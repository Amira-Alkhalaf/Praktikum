package swp_impl_acr.quizapy.Cursor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.View;

public class Cursor extends View {
    private ShapeDrawable cursor;
    private int width = 50;
    private int height = 50;

    public Cursor(Context context, int x, int y) {
        super(context);

        cursor = new ShapeDrawable(new OvalShape());
        cursor.getPaint().setColor(Color.RED);
        cursor.setBounds(x, y, x+width, y+height);
        cursor.setAlpha(90);
    }

    protected void onDraw(Canvas canvas) {
        cursor.draw(canvas);
    }

    public void moveCursor(int x, int y) {
        cursor.setBounds(x-(width/2), y-(height/2), x+(width/2), y+(height/2));
        invalidate();
    }

    public void setY(float y) {
        int x = cursor.getBounds().left+(width/2);

        moveCursor(x, (int)y);
    }

    public float getY() {
        return cursor.getBounds().top;
    }
}
