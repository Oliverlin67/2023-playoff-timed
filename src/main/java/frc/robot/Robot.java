// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenixpro.hardware.TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.simulation.PDPSim;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 * 改速度101行
 * 
 */
/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  private MecanumDrive m_drive;

  private CANSparkMax m_d1;
  private CANSparkMax m_d2;
  private CANSparkMax m_d3;
  private CANSparkMax m_d4;
  private CANSparkMax m_intake;
  private MotorController m_arm;

  private PowerDistribution m_pdp;

  private double maxDriveSpeed;
  private double intakeSpeed;
  private double armSpeed;
  private double[] currents = new double[8];

  private XboxController con1;
  //private XboxController con2;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {

    // 依照象限順序
    m_d1 = new CANSparkMax(Constants.motorDrive_1.getID(), MotorType.kBrushless); 
    m_d2 = new CANSparkMax(Constants.motorDrive_2.getID(), MotorType.kBrushless);
    m_d3 = new CANSparkMax(Constants.motorDrive_3.getID(), MotorType.kBrushless);
    m_d4 = new CANSparkMax(Constants.motorDrive_4.getID(), MotorType.kBrushless);

    m_intake = new CANSparkMax(Constants.motorIntake.getID(), MotorType.kBrushless);
    m_arm = new TalonFX(Constants.motorArm.getID());

    m_pdp = new PowerDistribution(0, ModuleType.kCTRE);

    m_drive = new MecanumDrive(m_d2, m_d3, m_d1, m_d4);

    //反的話註釋掉A改用B Group

    // A Group
    m_d1.setInverted(true);
    m_d4.setInverted(true);

    // B Group
    /*
    m_d2.setInverted(true);
    m_d3.setInverted(true);
     */

    con1 = new XboxController(0);
    //con2 = new XboxController(1);
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    //
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    //
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    maxDriveSpeed = 0.5;
    intakeSpeed = 0.5;
    armSpeed = 0.5;
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    if(con1.getLeftBumperPressed() && maxDriveSpeed > .4) {
      maxDriveSpeed -= .1;
    } else if(con1.getRightBumperPressed() && maxDriveSpeed < 1.) {
      maxDriveSpeed += .1;
    }

    if(con1.getAButtonPressed() && intakeSpeed > .4) {
      intakeSpeed -= .1;
    } else if(con1.getBButtonPressed() && intakeSpeed < 1.) {
      intakeSpeed += .1;
    }

    if(con1.getXButton()) m_intake.set(intakeSpeed);
    else if(con1.getYButton()) m_intake.set(-intakeSpeed);
    else m_intake.set(0);

    if(con1.getPOV() == 0) m_arm.set(armSpeed);
    else if(con1.getPOV() == 180) m_arm.set(-armSpeed);

    m_drive.setMaxOutput(maxDriveSpeed);
    m_drive.driveCartesian(con1.getLeftY(), con1.getLeftX(), con1.getRightX());

    SmartDashboard.putNumber("Total Current", m_pdp.getTotalCurrent());

    SmartDashboard.putNumber("Max Drive Speed", this.maxDriveSpeed);
    SmartDashboard.putNumber("Intake Speed", this.intakeSpeed);

    for(int i = 4; i < 12; i++) currents[i-4] = m_pdp.getCurrent(i);
    SmartDashboard.putNumberArray("Current per Port", currents);
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
