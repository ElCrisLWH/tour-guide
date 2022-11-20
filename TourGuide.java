import lejos.nxt.*;

// Tour Guide Lego Mindstorms prototype.
// Follows and remembers a path drawn by a black line in a white font.
// Then it is capable of repeating the same path without the drawn path.
// Specifications:
// Sensors ports should correspond to sensor position: 
// SensorPort.S2 -> Right Sensor
// SensorPort.S3 -> Left Sensor
// Motors should correspond to motor position:
// Motor.A -> Right Motor
// Motor.B -> Left Motor
public class TourGuide {
	int blackAndWhiteThreshold = 400;
	int outerWheelTurningSpeed = 100;
	int innerWheelTurningSpeed = 50;
	int straightLineSpeed = 180;
	int straightLineTime = 20;
	int turningTime = 20;
		
	boolean black = false;
	boolean white = true;
	boolean left = white;
	boolean right = white;
	
	LightSensor leftSensor = new LightSensor(SensorPort.S3);
	LightSensor rightSensor = new LightSensor(SensorPort.S2);
	
	public void executeMove(byte move){
		switch(move){
			case 0:
				Motor.A.setSpeed(straightLineSpeed);
				Motor.B.setSpeed(straightLineSpeed);
				Motor.A.forward();
				Motor.B.forward();
				try{
					Thread.sleep(straightLineTime);
				} catch (Exception e){
				}
			case 1:
				Motor.A.setSpeed(outerWheelTurningSpeed);
				Motor.B.setSpeed(innerWheelTurningSpeed);
				Motor.A.forward();
				Motor.B.backward();
				try{
					Thread.sleep(turningTime);
				} catch (Exception e){
				}
			case 2:
				Motor.A.setSpeed(innerWheelTurningSpeed);
				Motor.B.setSpeed(outerWheelTurningSpeed);
				Motor.A.backward();
				Motor.B.forward();
				try{
					Thread.sleep(turningTime);
				} catch (Exception e){
				}
			default: // or case 3
				Motor.A.stop();
				Motor.B.stop();
		}
	}

	public void run(){
		int leftIntensity;
		int rightIntensity;
		int whiteIntensity;
		int blackIntensity;

		byte move;
		Stack Record = new Stack();

		// Read with both sensors as the first reading always fails
		leftSensor.readNormalizedValue();
		rightSensor.readNormalizedValue();
		Button.ENTER.waitForPress();
		
		// Calibration
		System.out.println("Calibrate White Color. Set both sensors over White Color.");
		Button.ENTER.waitForPress();
		leftIntensity = leftSensor.readNormalizedValue();
		rightIntensity = rightSensor.readNormalizedValue();
		whiteIntensity = (leftIntensity + rightIntensity)/2;

		System.out.println("Calibrate Black Color. Set both sensors over Black Color.");
		Button.ENTER.waitForPress();
		leftIntensity = leftSensor.readNormalizedValue();
		rightIntensity = rightSensor.readNormalizedValue();
		blackIntensity = (leftIntensity + rightIntensity)/2;
		
		this.blackAndWhiteThreshold = (whiteIntensity + blackIntensity)/2;

		System.out.println("Set Tour Guide on starting position.");
		Button.ENTER.waitForPress();

		Motor.A.setSpeed(straightLineSpeed);
		Motor.B.setSpeed(straightLineSpeed);
		Motor.A.forward();
		Motor.B.forward();
		
		// Path recognition
		while(!Button.ESCAPE.isPressed()){
			while(left==white && right==white){
				move = 0;
				Record.push(move);
				this.executeMove(move);
				this.measure();
			}
			while(left==black && right==white){
				move = 1;
				Record.push(move);
				this.executeMove(move);
				this.measure();
			}
			while(left==white && right==black){
				move = 2;
				Record.push(move);
				this.executeMove(move);
				this.measure();
			}
			if(left==black && right==black){
				move = 3;
				Record.push(move);
				this.executeMove(move);
				break;
			}	
		}

		// Path replication
		Record.reverse();
		System.out.println("Set Tour Guide on replication position.");
		Button.ENTER.waitForPress();
		while(!Record.isEmpty()){
			move = Record.pop();
			this.executeMove(move);
		}
		
		Button.ENTER.waitForPress();
	}
	
	public boolean isWhite(int intensity){
		return intensity > blackAndWhiteThreshold;
	}

	// Updates the light intensity of both sensors.
	public void measure(){
		int leftIntensity = leftSensor.readNormalizedValue();
		int rightIntensity = rightSensor.readNormalizedValue();
		left = this.isWhite(leftIntensity);
		right = this.isWhite(rightIntensity);
	}
	
	public static void main (String[] args){
		new TourGuide().run();
	}
}

class List{
	byte header;
	byte value;
	List next;
	
	public List(){
		value = header;
		next = null;
	}
	
	public List(byte val){
		value = val;
		next = null;
	}
}

class Stack{
	List L;

	public Stack(){
		L = new List();
	}

	public void push(byte val){
		List pointer = new List(val);
		pointer.next = L.next;
		L.next = pointer;
	}

	public byte pop(){
		byte val = L.next.value;
		L.next = L.next.next;
		return val;
	}

	public boolean isEmpty(){
		return L.next == null;
	}

	public void reverse(){
		Stack reversed = new Stack();
		while(!this.isEmpty()){
			byte val = this.pop();
			reversed.push(val);
		}
		L = reversed.L;
	}
}