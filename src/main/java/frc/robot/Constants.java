package frc.robot;

public enum Constants {
    motorDrive_1(62),
    motorDrive_2(61),
    motorDrive_3(60),
    motorDrive_4(59),
    motorIntake(58), // 這是手掌Spark Max編號
    motorArm(0); // 這是手臂Talon編號

    // 這行底下不要改
    private Integer id;

    Constants(Integer id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }
}
