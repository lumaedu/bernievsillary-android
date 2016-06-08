package com.zkhaider.bernievshillary.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.zkhaider.bernievshillary.R;

/**
 * Created by ZkHaider on 6/7/16.
 */

public class BlurringView extends FrameLayout {

    private static final String TAG = BlurringView.class.getSimpleName();

    /**
     * Sampling Factors
     */
    private int mDownsampleFactor;
    private int mOverlayColor;

    /**
     * {@link View} resources.
     */
    private View mBlurredView;
    private int mBlurredViewWidth, mBlurredViewHeight;

    /**
     * {@link Bitmap} resources.
     */
    private boolean mDownsampleFactorChanged;
    private Bitmap mBitmapToBlur, mBlurredBitmap;
    private Canvas mBlurringCanvas;

    /**
     * {@link RenderScript} resources.
     */
    private RenderScript mRenderScript;
    private ScriptIntrinsicBlur mBlurScript;
    private Allocation mBlurInput, mBlurOutput;

    /*********************************************************************************************
     * Constructors
     *********************************************************************************************/

    public BlurringView(Context context) {
        this(context, null);
    }

    public BlurringView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setWillNotDraw(false);

        // Initialize resources
        final Resources resources = getResources();
        final int defaultBlurRadius = resources.getInteger(R.integer.default_blur_radius);
        final int defaultDownSampleFactor = resources.getInteger(R.integer.default_downsample_factor);
        final int defaultOverlayColor = resources.getColor(R.color.bg_glass);

        // RenderScript initialization
        initializeRenderScript(context);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PxBlurringView);
        setBlurredRadius(typedArray.getInt(R.styleable.PxBlurringView_blurRadius, defaultBlurRadius));
        setDownsampleFactor(typedArray.getInt(R.styleable.PxBlurringView_downsampleFactor, defaultDownSampleFactor));
        setOverlayColor(typedArray.getColor(R.styleable.PxBlurringView_overlayColor, defaultOverlayColor));

        // Clear typed array resource
        typedArray.recycle();

        this.invalidate();
    }

    /*********************************************************************************************
     * Overriden Methods
     *********************************************************************************************/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBlurredView != null) {
            if (prepare()) {
                // If the background of the blurred view is a color drawable, we use it to clear
                // the blurring canvas, which ensures that edges of the child views are blurred
                // as well; otherwise we clear the blurring canvas with a transparent color.
                if (mBlurredView.getBackground() != null && mBlurredView.getBackground() instanceof ColorDrawable){
                    mBitmapToBlur.eraseColor(((ColorDrawable) mBlurredView.getBackground()).getColor());
                } else {
                    mBitmapToBlur.eraseColor(Color.TRANSPARENT);
                }

                mBlurredView.draw(mBlurringCanvas);
                blur();

                canvas.save();
                canvas.translate(mBlurredView.getX() - getX(), mBlurredView.getY() - getY());
                canvas.scale(mDownsampleFactor, mDownsampleFactor);
                canvas.drawBitmap(mBlurredBitmap, 0, 0, null);
                canvas.restore();
            }
            canvas.drawColor(mOverlayColor);
        }
    }



    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mRenderScript != null) {
            // Clean up RenderScript resources
            mRenderScript.destroy();
        }
    }

    /*********************************************************************************************
     * Initialization Methods
     *********************************************************************************************/

    private void initializeRenderScript(Context context) {
        mRenderScript = RenderScript.create(context);
        mBlurScript = ScriptIntrinsicBlur.create(mRenderScript, Element.U8_4(mRenderScript));
    }

    protected boolean prepare() {
        // Get the height and width of the view to be blurred
        final int width = mBlurredView.getWidth();
        final int height = mBlurredView.getHeight();

        // Prepare if the following factors have changed
        if (mBlurringCanvas == null || mDownsampleFactorChanged || mBlurredViewWidth != width
                || mBlurredViewHeight != height) {
            mDownsampleFactorChanged = false;

            mBlurredViewWidth = width;
            mBlurredViewHeight = height;

            int scaledWidth = width / mDownsampleFactor;
            int scaledHeight = height / mDownsampleFactor;

            // The following manipulation is to avoid some RenderScript artifacts at the edge.
            scaledWidth = scaledWidth - scaledWidth % 4 + 4;
            scaledHeight = scaledHeight - scaledHeight % 4 + 4;

            /**
             * Create blurred bitmaps
             */
            if (mBlurredBitmap == null || mBlurredBitmap.getWidth() != scaledWidth
                    || mBlurredBitmap.getHeight() != scaledHeight) {
                mBitmapToBlur = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);

                if (mBitmapToBlur == null)
                    return false;

                mBlurredBitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);

                if (mBlurredBitmap == null)
                    return false;
            }

            mBlurringCanvas = new Canvas(mBitmapToBlur);
            mBlurringCanvas.scale(1f / mDownsampleFactor, 1f / mDownsampleFactor);
            mBlurInput = Allocation.createFromBitmap(mRenderScript, mBitmapToBlur,
                    Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            mBlurOutput = Allocation.createTyped(mRenderScript, mBlurInput.getType());
        }

        return true;
    }

    /*********************************************************************************************
     * Protected Inline Methods
     *********************************************************************************************/

    protected void blur() {
        mBlurInput.copyFrom(mBitmapToBlur);
        mBlurScript.setInput(mBlurInput);
        mBlurScript.forEach(mBlurOutput);
        mBlurOutput.copyTo(mBlurredBitmap);
    }

    /*********************************************************************************************
     * Getter/Setter Methods
     *********************************************************************************************/

    public void setBlurredView(View blurredView) {
        this.mBlurredView = blurredView;
        invalidate();
    }

    public void setBlurredRadius(int radius) {
        this.mBlurScript.setRadius(radius);
    }

    public void setDownsampleFactor(int factor) {
        if (factor <= 0) {
            throw new IllegalArgumentException("Downsample factor must be greater than 0.");
        }

        if (mDownsampleFactor != factor) {
            this.mDownsampleFactor = factor;
            this.mDownsampleFactorChanged = true;
        }
    }

    public void setOverlayColor(int color) {
        this.mOverlayColor = color;
    }

    /*********************************************************************************************
     * Deallocation Methods
     *********************************************************************************************/

    public void deallocateObjectsInView() {
        if (mBlurredBitmap != null)
            mBlurredBitmap.recycle();
        if (mBitmapToBlur != null)
            mBitmapToBlur.recycle();
        if (mBlurredView != null)
            mBlurredView = null;
    }

}