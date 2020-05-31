package com.example.soptask2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedHashMap;
import java.util.Map;

public class GameScreen extends View {

    private double canvasHeight;
    private double canvasWidth;
    private double boxSize;
    private Paint blackStroke;
    private Paint blueStroke;
    private Paint redStroke;
    private Paint blackStrike;
    private Paint playerText;
    private Paint turnText;
    private Paint gameOverText;
    private Path gridPath;
    private Path circlePath;
    private Path crossPath;
    private Path strikePath;
    MainActivity mainActivity;
    private Map<Integer, Boolean> gridChecker;
    private int turn;
    private int boxesChecked;
    private int winner;
    private Map<Integer, Integer> row1, row2, row3, column1, column2, column3, diagonal1, diagonal2;
    private boolean gameState;
    private String name1, name2;

    private static final String TAG = "GameScreen";

    public GameScreen(Context applicationContext, MainActivity mainActivity) {
        super(applicationContext);
        this.mainActivity = mainActivity;

        Log.d(TAG, "GameScreen: started");

        name1 = mainActivity.getName1();
        name2 = mainActivity.getName2();

        blackStroke = new Paint();
        blackStroke.setColor(Color.BLACK);
        blackStroke.setStyle(Paint.Style.STROKE);
        blackStroke.setStrokeWidth(15);
        blackStroke.setAntiAlias(true);

        blackStrike = new Paint();
        blackStrike.setStyle(Paint.Style.STROKE);
        blackStrike.setColor(Color.BLACK);
        blackStrike.setStrokeWidth(10);
        blackStrike.setAntiAlias(true);

        blueStroke = new Paint();
        blueStroke.setColor(Color.BLUE);
        blueStroke.setStyle(Paint.Style.STROKE);
        blueStroke.setStrokeWidth(16);
        blueStroke.setAntiAlias(true);

        redStroke = new Paint();
        redStroke.setColor(Color.RED);
        redStroke.setStrokeWidth(16);
        redStroke.setStyle(Paint.Style.STROKE);
        redStroke.setAntiAlias(true);

        playerText = new Paint();
        playerText.setColor(Color.BLACK);
        //playerText.setTextSize(80);
        playerText.setAntiAlias(true);

        turnText = new Paint();
        turnText.setColor(Color.BLACK);
        //turnText.setTextSize(40);
        playerText.setAntiAlias(true);

        gameOverText = new Paint();
        gameOverText.setColor(Color.BLACK);
        gameOverText.setAntiAlias(true);

        gridPath = new Path();
        circlePath = new Path();
        crossPath = new Path();
        strikePath = new Path();

        gridChecker = new LinkedHashMap<>();
        for(int i = 1; i <= 9; i++)
            gridChecker.put(i, false);

        row1 = new LinkedHashMap<>();
        row2 = new LinkedHashMap<>();
        row3 = new LinkedHashMap<>();
        column1 = new LinkedHashMap<>();
        column2 = new LinkedHashMap<>();
        column3 = new LinkedHashMap<>();
        diagonal1 = new LinkedHashMap<>();
        diagonal2 = new LinkedHashMap<>();

        trackerInitializations();

        boxesChecked = 0;
        winner = 0;
        turn = 1;
        gameState = true;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xPos = event.getX();
        float yPos = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(gameState) {
                    if (xPos > boxSize && xPos < (canvasWidth - boxSize) && yPos > (canvasHeight / 2 - 1.5 * boxSize) && yPos < (canvasHeight / 2 + 1.5 * boxSize)) {
                        checkBox(xPos, yPos);
                        Log.d(TAG, "onTouchEvent: touched inside grid");
                    }
                    postInvalidate();
                    return true;
                }
            default:
                return false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();

        drawGrid(canvas);
        canvas.drawPath(circlePath, blueStroke);
        canvas.drawPath(crossPath, redStroke);
        canvas.drawPath(strikePath, blackStrike);
        drawTurns(canvas);

    }

    public void drawGrid(Canvas canvas) {
        boxSize = canvasWidth / 5;
        double lineLength = boxSize * 3;

        gridPath.moveTo((float)(boxSize), (float)(canvasHeight/2 - boxSize/2));
        gridPath.lineTo((float)(boxSize + lineLength), (float)(canvasHeight/2 - boxSize/2));

        gridPath.moveTo((float)(boxSize), (float)(canvasHeight/2 + boxSize/2));
        gridPath.lineTo((float)(boxSize + lineLength), (float)(canvasHeight/2 + boxSize/2));

        gridPath.moveTo((float)(boxSize*2), (float)(canvasHeight/2 - 1.5*boxSize));
        gridPath.lineTo((float)(boxSize*2), (float)(canvasHeight/2 + 1.5*boxSize));

        gridPath.moveTo((float)(boxSize*3), (float)(canvasHeight/2 - 1.5*boxSize));
        gridPath.lineTo((float)(boxSize*3), (float)(canvasHeight/2 + 1.5*boxSize));

        canvas.drawPath(gridPath, blackStroke);

    }

    public void checkBox(float x, float y) {

        int row, column, boxNumber;

        if(x < boxSize * 2)
            column = 1;
        else if(x < boxSize * 3)
            column = 2;
        else
            column = 3;

        if(y < canvasHeight / 2 - boxSize / 2)
            row = 1;
        else if(y < canvasHeight / 2 + boxSize / 2)
            row = 2;
        else
            row = 3;

        boxNumber = (row-1)*3 + column;

        if(!gridChecker.get(boxNumber)) {
            boxesChecked++;
            gridChecker.put(boxNumber, true);
            if(turn == 1)
                drawCrosses(row, column);
            else
                drawCircles(row, column);

            if(row == 1) {
                row1.put(turn, row1.get(turn) + 1);
                if(row1.get(turn) == 3) {
                    drawStrike("row", 1);
                }
            }
            else if(row == 2) {
                row2.put(turn, row2.get(turn) + 1);
                if(row2.get(turn) == 3) {
                    drawStrike("row", 2);
                }
            }
            else {
                row3.put(turn, row3.get(turn) + 1);
                if(row3.get(turn) == 3) {
                    drawStrike("row", 3);
                }
            }

            if(column == 1) {
                column1.put(turn, column1.get(turn) + 1);
                if(column1.get(turn) == 3) {
                    drawStrike("column", 1);
                }

            }
            else if(column == 2) {
                column2.put(turn, column2.get(turn) + 1);
                if(column2.get(turn) == 3) {
                    drawStrike("column", 2);
                }
            }
            else {
                column3.put(turn, column3.get(turn) + 1);
                if(column3.get(turn) == 3) {
                    drawStrike("column", 3);
                }
            }

            if(row == column) {
                diagonal1.put(turn, diagonal1.get(turn) + 1);
                if(diagonal1.get(turn) == 3) {
                    drawStrike("diagonal", 1);
                }
            }

            if(row + column == 4) {
                diagonal2.put(turn, diagonal2.get(turn) + 1);
                if(diagonal2.get(turn) == 3) {
                    drawStrike("diagonal", 2);
                }
            }

            if(boxesChecked == 9)
                gameState = false;
            switchTurn();
        }

    }

    public void drawCrosses(int row, int column) {
        float xLeft, yTop, xRight, yBottom;

        yTop = (float) (canvasHeight / 2 + (row-2)*boxSize - boxSize / 2 + 30 );
        xLeft = (float) (canvasWidth / 2 + (column-2)*boxSize - boxSize / 2 + 30);
        yBottom = (float) (canvasHeight / 2 + (row-2)*boxSize + boxSize / 2 - 30);
        xRight = (float) (canvasWidth / 2 + (column-2)*boxSize + boxSize / 2 - 30);

        crossPath.moveTo(xLeft, yTop);
        crossPath.lineTo(xRight, yBottom);
        crossPath.moveTo(xRight, yTop);
        crossPath.lineTo(xLeft, yBottom);

    }

    public void drawCircles(int row, int column) {
        float xCentre, yCentre;

        yCentre = (float) (canvasHeight / 2 + (row-2)*boxSize);
        xCentre = (float) (canvasWidth / 2 + (column-2)*boxSize);

        circlePath.addCircle(xCentre, yCentre, (float)(boxSize/2 - 30), Path.Direction.CW);

    }

    public void switchTurn() {
        if(turn == 1)
            turn = 2;
        else
            turn = 1;
    }

    public void trackerInitializations() {
        row1.put(1, 0);
        row1.put(2, 0);
        row2.put(1, 0);
        row2.put(2, 0);
        row3.put(1, 0);
        row3.put(2, 0);
        column1.put(1,0);
        column1.put(2, 0);
        column2.put(1,0);
        column2.put(2, 0);
        column3.put(1,0);
        column3.put(2, 0);
        diagonal1.put(1,0);
        diagonal1.put(2,0);
        diagonal2.put(1,0);
        diagonal2.put(2,0);
    }

    public void drawStrike(String rcd, int num) {
        gameState = false;
        winner = turn;

        if("row".equals(rcd)) {
            strikePath.moveTo((float) (boxSize + 10), (float) (canvasHeight / 2 + (num-2)*boxSize));
            strikePath.lineTo((float) (canvasWidth - boxSize - 10), (float) (canvasHeight / 2 - boxSize));
        }
        else if("column".equals(rcd)) {
            strikePath.moveTo((float) (canvasWidth / 2 + (num-2)*boxSize), (float) (canvasHeight / 2 - 1.5*boxSize + 10));
            strikePath.lineTo((float) (canvasWidth / 2 + (num-2)*boxSize), (float) (canvasHeight / 2 + 1.5*boxSize - 10));
        }
        else {
            float xLeft = (float) (boxSize + 20), xRight = (float) (canvasWidth - boxSize - 20);
            float yTop = (float) (canvasHeight / 2 - 1.5 * boxSize + 20), yBottom = (float) (canvasHeight / 2 + 1.5 * boxSize - 20);

            if(num == 1) {
                strikePath.moveTo(xLeft, yTop);
                strikePath.lineTo(xRight, yBottom);
            }
            else {
                strikePath.moveTo(xLeft, yBottom);
                strikePath.lineTo(xRight, yTop);
            }
        }
    }

    public void drawTurns(Canvas canvas) {
        playerText.setTextSize((float) (canvasWidth / 13.5));
        turnText.setTextSize(playerText.getTextSize() / 2);
        gameOverText.setTextSize((float)(playerText.getTextSize()*1.5));

        canvas.drawText(name1, (float) (boxSize/2), (float) (canvasHeight / 8), playerText);
        canvas.drawText(name2, (float) (canvasWidth/2 + boxSize/2), (float) (canvasHeight / 8), playerText);

        if(turn == 1 && gameState) {
            canvas.drawText("(Your turn)", (float) (boxSize/2), (float) (canvasHeight / 8 + 100), turnText);
        }
        else if(turn == 2 && gameState) {
            canvas.drawText("(Your turn)", (float) (canvasWidth/2 + boxSize/2), (float) (canvasHeight / 8 + 100), turnText);
        }

        if(!gameState) {
            if(winner == 1) {
                canvas.drawText(name1 + " Wins!", (float) (boxSize*0.75), (float) (canvasHeight*0.8), gameOverText);
                mainActivity.playWin1();
            }
            else if(winner == 2) {
                canvas.drawText(name2 + " Wins!", (float) (boxSize*0.75), (float) (canvasHeight*0.8), gameOverText);
                mainActivity.playWin2();
            }
            else {
                canvas.drawText("Draw", (float) (boxSize*1.8), (float) (canvasHeight*0.8), gameOverText);
                mainActivity.playDraw();
            }
        }

    }

}
