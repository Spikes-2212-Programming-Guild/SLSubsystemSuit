package com.spikes2212.genericsubsystems.drivetrains.commands;

import java.util.function.Supplier;

import com.spikes2212.genericsubsystems.drivetrains.TankDrivetrain;
import com.spikes2212.utils.PIDSettings;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * This command moves a {@link TankDrivetrain} using wpilib's
 * {@link PIDController}. It also waits a specified amount of time after the
 * error is within the given tolerance before stopping the PID Loop to make sure
 * the drivetrain doesn't go past and remain beyond the setpoint.
 *
 * @author Omri "Riki" Cohen
 * @see TankDrivetrain
 * @see PIDController
 * @see PIDSettings
 */
public class DriveTankWithPID extends Command {

	protected TankDrivetrain tankDrivetrain;
	protected Supplier<Double> leftSetpoint;
	protected Supplier<Double> rightSetpoint;
	protected PIDSettings PIDSettings;
	protected PIDSource leftSource;
	protected PIDSource rightSource;
	protected PIDController leftMovmentControl;
	protected PIDController rightMovmentControl;
	protected double lastTimeNotOnTarget;

	/**
	 * Gets the PIDSetting the PIDController uses for this command.
	 * 
	 * @return The PIDSetting the PIDController uses
	 * @see PIDSettings
	 * @see PIDController
	 */
	public PIDSettings getPIDSetting() {
		return PIDSettings;
	}

	/**
	 * Sets the tolerance for error of this PID loop.
	 * <p>
	 * This tolerance defines when this PID loop ends: This command will end
	 * after the difference between the setpoint and the current position is
	 * within the tolerance for the amount of time specified by
	 * {@link #setWaitTime(double)} straight.
	 * </p>
	 *
	 * @param tolerance
	 *            The new tolerance to set. If 0, this PID loop will never end.
	 * @see PIDController#setAbsoluteTolerance(double)
	 * @see PIDController#getTolerance
	 */
	public void setTolerance(double tolerance) {
		PIDSettings.setTolerance(tolerance);
	}

	/**
	 * Sets the time this PID loop will wait while within tolerance of the
	 * setpoint before ending.
	 * <p>
	 * The PID control of the subsystem continues while waiting. <br/>
	 * If wait time is set to 0, the command won't wait.
	 * </p>
	 *
	 * @param waitTime
	 *            the new wait time, in seconds.
	 */
	public void setWaitTime(double waitTime) {
		PIDSettings.setWaitTime(waitTime);
	}

	/**
	 * This constructs a new {@link DriveTankWithPID} using {@link PIDSource}s
	 * the setpoints for each side, the PID coefficients this command's PID loop
	 * should have, and the tolerance for error.
	 *
	 * @param drivetrain
	 *            the {@link TankDrivetrain} this
	 *            command requires and moves.
	 * @param leftSource
	 *            the {@link PIDSource} this command uses to get feedback for
	 *            the PID Loop for the left side.
	 * @param rightSource
	 *            the {@link PIDSource} this command uses to get feedback for
	 *            the PID Loop for the right side.
	 * @param leftSetpoint
	 *            a {@link Supplier} supplying the the target point for the left side of
	 *            the drivetrain.
	 *            <p>
	 *            This command will try to move drivetrain's left side until it
	 *            reaches the latest value supplied by setpoint. setpoint should
	 *            be using the same units as leftSource.
	 *            </p>
	 * @param rightSetpoint
	 *            a {@link Supplier} supplying the the target point for the right side
	 *            of the drivetrain.
	 *            <p>
	 *            This command will try to move drivetrain's right side until it
	 *            reaches the latest value supplied by setpoint. setpoint should
	 *            be using the same units as rightSource.
	 *            </p>
	 * @param PIDSettings
	 *            the {@link PIDSettings} this command's PIDController needs.
	 * 
	 * @see PIDController
	 */
	public DriveTankWithPID(TankDrivetrain drivetrain, PIDSource leftSource, PIDSource rightSource,
			Supplier<Double> leftSetpoint, Supplier<Double> rightSetpoint, PIDSettings PIDSettings) {
		requires(drivetrain);
		this.tankDrivetrain = drivetrain;
		this.leftSource = leftSource;
		this.rightSource = rightSource;
		this.leftSetpoint = leftSetpoint;
		this.rightSetpoint = rightSetpoint;
		this.PIDSettings = PIDSettings;
	}

	/**
	 * This constructs a new {@link DriveTankWithPID} using {@link PIDSource}s
	 * the setpoints for each side, the PID coefficients this command's PID loop
	 * should have, and the tolerance for error.
	 *
	 * @param drivetrain
	 *            the {@link edu.wpi.first.wpilibj.command.Subsystem} this
	 *            command requires and moves.
	 * @param leftSource
	 *            the {@link PIDSource} this command uses to get feedback for
	 *            the PID Loop for the left side.
	 * @param rightSource
	 *            the {@link PIDSource} this command uses to get feedback for
	 *            the PID Loop for the right side.
	 * @param leftSetpoint
	 *            the target point for the left side of the drivetrain.
	 *            <p>
	 *            This command will try to move drivetrain's left side until it
	 *            reaches the setpoint. setpoint should be using the same units
	 *            as leftSource.
	 *            </p>
	 * @param rightSetpoint
	 *            the target point for the right side of the drivetrain.
	 *            <p>
	 *            This command will try to move drivetrain's right side until it
	 *            reaches the setpoint. setpoint should be using the same units
	 *            as rightSource.
	 *            </p>
	 * @param PIDSettings
	 *            the {@link PIDSettings} this command's PIDController needs.
	 * 
	 * @see PIDController
	 */
	public DriveTankWithPID(TankDrivetrain drivetrain, PIDSource leftSource, PIDSource rightSource, double leftSetpoint,
			double rightSetpoint, PIDSettings PIDSettings) {
		this(drivetrain, leftSource, rightSource, () -> leftSetpoint, () -> rightSetpoint, PIDSettings);
	}

	/**
	 * This constructs a new {@link DriveTankWithPID} using {@link PIDSource}s
	 * the setpoints for each side, the PID coefficients this command's PID loop
	 * should have, and the tolerance for error.
	 *
	 * @param drivetrain
	 *            the {@link edu.wpi.first.wpilibj.command.Subsystem} this
	 *            command requires and moves.
	 * @param leftSource
	 *            the {@link PIDSource} this command uses to get feedback for
	 *            the PID Loop for the left side.
	 * @param rightSource
	 *            the {@link PIDSource} this command uses to get feedback for
	 *            the PID Loop for the right side.
	 * @param setPoint
	 *            the target point of this command.
	 *            <p>
	 *            This command will try to move drivetrain until both sides
	 *            reache the setpoint. setpoint should be using the same units
	 *            as drivetrain's {@link PIDSource}s.
	 *            </p>
	 * @param PIDSettings
	 *            the {@link PIDSettings} this command's PIDController needs.
	 * 
	 * @see PIDController
	 */
	public DriveTankWithPID(TankDrivetrain drivetrain, PIDSource leftSource, PIDSource rightSource, double setpoint,
			PIDSettings PIDSettings) {
		this(drivetrain, leftSource, rightSource, () -> setpoint, () -> setpoint, PIDSettings);
	}

	/**
	 * This constructs a new {@link DriveTankWithPID} using {@link PIDSource}s
	 * the setpoints for each side, the PID coefficients this command's PID loop
	 * should have, and the tolerance for error.
	 *
	 * @param drivetrain
	 *            the {@link edu.wpi.first.wpilibj.command.Subsystem} this
	 *            command requires and moves.
	 * @param leftSource
	 *            the {@link PIDSource} this command uses to get feedback for
	 *            the PID Loop for the left side.
	 * @param rightSource
	 *            the {@link PIDSource} this command uses to get feedback for
	 *            the PID Loop for the right side.
	 * @param setPoint
	 *            a {@link Supplier} supplying the target point of this command.
	 *            <p>
	 *            This command will try to move drivetrain until both sides
	 *            reache the setpoint. setpoint should be using the same units
	 *            as drivetrain's {@link PIDSource}s.
	 *            </p>
	 * @param PIDSettings
	 *            the {@link PIDSettings} this command's PIDController needs.
	 * 
	 * @see PIDController
	 */
	public DriveTankWithPID(TankDrivetrain drivetrain, PIDSource leftSource, PIDSource rightSource,
			Supplier<Double> setpoint, PIDSettings PIDSettings) {
		this(drivetrain, leftSource, rightSource, setpoint, setpoint, PIDSettings);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		leftMovmentControl = new PIDController(PIDSettings.getKP(), PIDSettings.getKI(), PIDSettings.getKD(),
				leftSource, tankDrivetrain::setLeft);
		leftMovmentControl.setAbsoluteTolerance(PIDSettings.getTolerance());
		leftMovmentControl.setSetpoint(this.leftSetpoint.get());
		leftMovmentControl.setOutputRange(-1, 1);
		rightMovmentControl = new PIDController(PIDSettings.getKP(), PIDSettings.getKI(), PIDSettings.getKD(),
				rightSource, tankDrivetrain::setRight);
		rightMovmentControl.setAbsoluteTolerance(PIDSettings.getTolerance());
		rightMovmentControl.setSetpoint(this.rightSetpoint.get());
		rightMovmentControl.setOutputRange(-1, 1);
		leftMovmentControl.enable();
		rightMovmentControl.enable();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		double newSetPointLeft = leftSetpoint.get();
		double newSetPointRight = rightSetpoint.get();
		if (newSetPointLeft != leftMovmentControl.getSetpoint())
			leftMovmentControl.setSetpoint(newSetPointLeft);
		if (newSetPointRight != rightMovmentControl.getSetpoint())
			rightMovmentControl.setSetpoint(newSetPointRight);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		if (!leftMovmentControl.onTarget() || !rightMovmentControl.onTarget()) {
			lastTimeNotOnTarget = Timer.getFPGATimestamp();
		}
		return Timer.getFPGATimestamp() - lastTimeNotOnTarget >= PIDSettings.getWaitTime();
	}

	// Called once after isFinished returns true
	protected void end() {
		leftMovmentControl.disable();
		rightMovmentControl.disable();
		tankDrivetrain.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}

}
