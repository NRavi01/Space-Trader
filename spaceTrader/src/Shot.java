public class Shot {
    private int x;
    private int y;
    private int speed;
    private int direction; //0 for right, 1 for left
    private int power;
    private double angle = 0;

    public Shot(int x, int y, int speed, int direction, int power) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.direction = direction;
        this.power = power;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getSpeedX() {
        return speed * Math.cos(angle);
    }

    public double getSpeedY() {
        return speed * Math.sin(angle);
    }

    public int getPower() {
        return power;
    }

    public int getDirection() {
        return direction;
    }

    public int getSpeed() {
        return speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
