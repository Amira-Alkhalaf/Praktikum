package swp_impl_acr.quizapy.Cursor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.View;

public class Cursor extends View {
    private ShapeDrawable cursor;
    private int width = 50;
    private int height = 50;

    public Cursor(Context context, int x, int y) {
        super(context);

        cursor = new ShapeDrawable(new OvalShape());
        cursor.getPaint().setColor(Color.RED);
        cursor.setAlpha(50);
        cursor.setBounds(x, y, x+width, y+height);
    }

    protected void onDraw(Canvas canvas) {
        cursor.draw(canvas);
    }

    public void moveCursor(int x, int y){
        cursor.setBounds(x, y, x+width, y+height);
    }
}
