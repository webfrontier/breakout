package jp.co.webfrontier.breakout;

import android.graphics.Color;

/**
 * ブロック（通常）
 */
public class BrickNormal extends Brick {
    /**
     * デバッグログ用タグ
     */
    private static final String TAG = "BrickNormal";

    /** A-01. 複数回当てないと破壊できないブロックを作る
     * ブロック耐久度の導入
     * ブロックが壊れるまでのボールヒット回数を導入する
     * ブロック耐久度の違うブロックをランダムで生成する
     * crashメソッドをオーバーライドして、ブロック耐久度と比較する
     */
    /**
     * ブロックの耐久度
     */
    private enum Robustness {
        /**
         * 通常のブロック
         */
        NORMAL("通常のブロック", 1, Color.GRAY),
        /**
         * ちょっと強いブロック
         */
        STRONGER("ちょっと強いブロック", 3, Color.CYAN),
        /**
         * もっと強いブロック
         */
        STRONGEST("もっと強いブロック", 5, Color.RED);

        /**
         * 耐久度の名前
         */
        private final String name;

        /**
         * 耐久度
         */
        private final int value;

        /**
         * ブロックの色
         */
        private final int color;

        /**
         * コンストラクタ
         *
         * @param name 耐久度の名前
         * @param value 耐久度値
         * @param color ブロックの色
         *
         */
        private Robustness(final String name, final int value, final int color)
        {
            this.name = name;
            this.value = value;
            this.color = color;
        }

        /**
         * ブロックの耐久度の名前を取得する
         *
         * @return 耐久度名
         */
        String getName() { return name; }

        /**
         * ブロックの耐久度の値を取得する
         *
         * @return 耐久度の値
         */
        int getValue()
        {
            return value;
        }

        /**
         * ブロックの色を取得する
         *
         * @return ブロックの色
         */
        int getColor()
        {
            return color;
        }
    }
    /**
     * ブロックの耐久度
     */
    private Robustness robustness = Robustness.NORMAL;

    /** A-01. 複数回当てないと破壊できないブロックを作る
     * ブロック耐久度の導入
     * ブロックが壊れるまでのボールヒット回数を導入する
     * ブロック耐久度の違うブロックをランダムで生成する
     * crashメソッドをオーバーライドして、ブロック耐久度と比較する
     */
    /**
     * ブロックにボールが当たった回数
     */
    private int hitCount = 0;

    /**
     * コンストラクタ
     *
     */
    public BrickNormal() {
        super();
        initialize();
    }

    /**
     * コンストラクタ
     *
     * @param x ブロック位置(X座標)
     * @param y ブロック位置(Y座標)
     */
    public BrickNormal(int x, int y) {
        super(x, y);
        initialize();
    }

    /**
     * ブロックを初期化する
     *
     */
    public void initialize() {
        // ブロックの種別を上書きする
        type = Type.NORMAL;

        /** A-01. 複数回当てないと破壊できないブロックを作る
         * ブロック耐久度の導入
         * ブロックが壊れるまでのボールヒット回数を導入する
         * ブロック耐久度の違うブロックをランダムで生成する
         * crashメソッドをオーバーライドして、ブロック耐久度と比較する
         */
        // ブロックの耐久度をランダムで設定する
        final int i = (int)(Math.random()*10) % 3;
        switch(i) {
            case 0:
                this.robustness = Robustness.STRONGEST;
                break;
            case 1:
                this.robustness = Robustness.STRONGER;
                break;
            case 2:
            default:
                this.robustness = Robustness.NORMAL;
                break;
        }

        // ペインターへ色設定
        color = robustness.getColor();
        painter.setColor(color);
    }

    /** A-01. 複数回当てないと破壊できないブロックを作る
     * ブロック耐久度の導入
     * ブロックが壊れるまでのボールヒット回数を導入する
     * ブロック耐久度の違うブロックをランダムで生成する
     * crashメソッドをオーバーライドして、ブロック耐久度と比較する
     */
    @Override
    public void crash() {
        hitCount++;
        final int r = robustness.getValue();
        if(r == hitCount) {
            broken = true;
            /** A-04. 効果音を鳴らす
             * ゲームの効果音をつける
             * ブロックを破壊したとき, 破壊できないブロックに当たったとき,パッドで反射したとき
             * ボールをロストしたとき, ゲームをクリアしたとき, ゲームオーバーになったとき
             */
            SoundController.playHitBrick();
        }
    }

    /** A-05. ゲームの得点を表示する
     * 得点表示用のUI部品(TextView)を配置する
     * ブロックの耐久度ごとに破壊したときに得られる得点を決める
     * 得点を加算していき表示する
     */
    /**
     * ブロックを破壊したときに得られる得点を取得する
     *
     * @return 得点
     */
    @Override
    public int getPoint() {
        if(!broken) {
            return 0;
        }
        switch(robustness) {
            case NORMAL:
                return 1;
            case STRONGER:
                return 5;
            case STRONGEST:
                return 10;
            default:
                return 0;
        }
    }
}