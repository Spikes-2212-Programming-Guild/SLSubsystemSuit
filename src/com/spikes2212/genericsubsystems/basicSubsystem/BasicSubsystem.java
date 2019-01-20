package com.spikes2212.genericsubsystems.basicSubsystem;

import java.util.function.Consumer;
import java.util.function.Predicate;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * This class represents a generic subsystem that moves within a limitation, or
 * without one.
 *
 * @author Omri "Riki" Cohen
 */
public class BasicSubsystem extends Subsystem {

	/**
	 * This function, when applied to a certain double speed returns true if
	 * this subsystem can move at that speed
	 * A {@link Predicate<Double>} to store the limits of the subsystem's speed.
	 */
	public final Predicate<Double> canMove;
	
	/**
	 * A {@link Consumer} to represent the movement of the {@link BasicSubsystem}.
	 */
	protected final Consumer<Double> speedConsumer;
	private double currentSpeed = 0;
	
	/**
	 * The maximum change between the current speed and the speed
	 * the {@link BasicSubsystem} is being set to.
	 */
	private double maxChange = 1;

	/**
	 * Constructor that recieves a {@link Consumer} for the movement component and a {@link Predicate<Double>}
	 * that represents the limits of the subsystem's speed.
	 * 
	 * @param speedConsumer
	 *            the component using the speed (usually a motor/motors).
	 * @param canMove
	 *            the limitation on the movement, which depends on the speed.
	 */
	public BasicSubsystem(Consumer<Double> speedConsumer, Predicate<Double> canMove) {
		this.canMove = canMove;
		this.speedConsumer = speedConsumer;
	}

	/**
	 * Constructor that recieves a {@link Consumer} for the movement component and a {@link Predicate<Double>}
	 * that represents the limits of the subsystem's speed.
	 *
	 * @param name
	 * 			  the name of the subsystem that will be displayed on the dashboard
	 * @param speedConsumer
	 *            the component using the speed (usually a motor/motors).
	 * @param canMove
	 *            the limitation on the movement, which depends on the speed.
	 */
	public BasicSubsystem(String name, Consumer<Double> speedConsumer, Predicate<Double> canMove) {
		super(name);
		this.canMove = canMove;
		this.speedConsumer = speedConsumer;
	}

	/**
	 * Moves this {@link BasicSubsystem} with the given speed, as long as it is within the limits
	 * specified when this {@link BasicSubsystem} was constructed and within the limits of the maxChange
	 * from the previous speed.
	 *
	 * @param speed
	 *            the speed to move the subsystem with.
	 */
	public void move(double speed) {
		if (canMove.test(speed)) {
			if (speed > 1)
				speed = 1;
			else if (speed < -1)
				speed = -1;
			if (Math.abs(speed - currentSpeed) > maxChange)
				speed = currentSpeed + maxChange * Math.signum(speed - currentSpeed);
			speedConsumer.accept(speed);
			this.currentSpeed = speed;
		}
	}

	/**
	 * Stops this subsystem's movement.
	 */
	public void stop() {
		move(0);
	}

	/**
	 * Return the current speed of this {@link BasicSubsystem}.
	 *
	 * @return the current speed of this {@link BasicSubsystem}.
	 */
	public double getSpeed() {
		return currentSpeed;
	}
	
	/**
	 * Return the max change in speed of this {@link BasicSubsystem}.
	 *
	 * @return the max change in speed of this {@link BasicSubsystem}.
	 */
	public double getMaxChange() {
		return maxChange;
	}
	
	/**
	 * Set the maximum change in speed this {@link BasicSubsystem} will tolerate.
	 *
	 * @param maxChange
	 * 				The new maximum change in speed this
	 * 				{@link BasicSubsystem} will tolerate.
	 */
	public void setMaxChange(double maxChange) {
		this.maxChange = maxChange;
	}
	
	/**
	 * @see Sets the default command. If this is not called, or is called with null, then
	 * there will be no default command for the subsystem.
	 */
	public void setDefaultCommand(Command defaultCommand) {
		super.setDefaultCommand(defaultCommand);
	}

	public void initDefaultCommand() {

	}

}
