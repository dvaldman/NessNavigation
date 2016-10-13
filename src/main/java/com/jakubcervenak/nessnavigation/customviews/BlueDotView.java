package com.jakubcervenak.nessnavigation.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.jakubcervenak.nessnavigation.R;


public class BlueDotView extends SubsamplingScaleImageView {
        private float radius = 1.0f;
        private PointF dotCenter = null;
        private PointF mePoint = null;

    public void setMePoint(PointF mePoint) {
        this.mePoint = mePoint;
    }

    public void setRadius(float radius) {
            this.radius = radius;
        }

        public void setDotCenter(PointF dotCenter) {
            this.dotCenter = dotCenter;
        }

        public BlueDotView(Context context) {
            this(context, null);
        }

        public BlueDotView(Context context, AttributeSet attr) {
            super(context, attr);
            initialise();
        }

        private void initialise() {
            setWillNotDraw(false);
            setPanLimit(SubsamplingScaleImageView.PAN_LIMIT_CENTER);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (!isReady()) {
                return;
            }

            if (dotCenter != null) {
                PointF vPoint = sourceToViewCoord(dotCenter);
                float scaledRadius = getScale() * radius;
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(getResources().getColor(R.color.colorPrimary));
                canvas.drawCircle(vPoint.x, vPoint.y, scaledRadius, paint);
            }

            if (mePoint != null) {
                PointF vPoint = sourceToViewCoord(mePoint);
                float scaledRadius = getScale() * radius;
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(getResources().getColor(R.color.black));
                canvas.drawCircle(vPoint.x, vPoint.y, scaledRadius, paint);
            }
        }
}

