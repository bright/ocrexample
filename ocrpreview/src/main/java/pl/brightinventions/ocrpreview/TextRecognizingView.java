package pl.brightinventions.ocrpreview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.IOException;

public class TextRecognizingView extends FrameLayout {

    private static final String TAG = "TextRecognizingView";
    private final CameraSourcePreview cameraSourcePreview;
    private final GraphicOverlay graphicOverlay;
    private CameraSource cameraSource;

    public TextRecognizingView(@NonNull Context context) {
        this(context, null);
    }

    public TextRecognizingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextRecognizingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TextRecognizingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        cameraSourcePreview = new CameraSourcePreview(context, attrs);
        addView(cameraSourcePreview, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        graphicOverlay = new GraphicOverlay(context, attrs);
        addView(graphicOverlay, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void createCameraSourceForText() {
        if (cameraSource == null) {
            cameraSource = new CameraSource(getContext(), graphicOverlay);
            cameraSource.setFacing(CameraSource.CAMERA_FACING_BACK);
            cameraSource.setMachineLearningFrameProcessor(new TextRecognitionProcessor());
        }
    }

    public void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (cameraSourcePreview == null) {
                    Log.d(TAG, "resume: Preview is null");
                    return;
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null");
                    return;
                }
                cameraSourcePreview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }
}
