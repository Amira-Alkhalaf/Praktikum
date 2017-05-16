package swp_impl_acr.quizapy.Cursor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.View;

/**
 * class to create a simple round cursor
 */
public class Cursor extends View {
    private ShapeDrawable cursor;
    private int width = 50;
    private int height = 50;

    /**
     * constructor
     * @param context
     * @param x left
     * @param y top
     */
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

    /**
     * the center of the cursor is placed at position x,y
     * @param x
     * @param y
     */
    public void moveCursor(int x, int y) {
        cursor.setBounds(x-(width/2), y-(height/2), x+(width/2), y+(height/2));
        invalidate();
    }

    /**
     * moves the cursor vertically to position y
     * @param y
     */
    public void setY(float y) {
        int x = cursor.getBounds().left+(width/2);

        moveCursor(x, (int)y);
    }

    /**
     * returns top of the cursor
     * @return
     */
    public float getY() {
        return cursor.getBounds().top;
    }

    /**
     * moves the cursor horizontally to position x
     * @param x
     */
    public void setX(float x) {
        int y = cursor.getBounds().top+(width/2);

        moveCursor((int)x, y);
    }

    /**
     * returns left of the cursor
     * @return
     */
    public float getX() {
        return cursor.getBounds().left;
    }
}
