import java.io.*;
import java.text.DecimalFormat;

public class TrackInfo {
	//a method to check if the address is available
	public static void checkFile(String location) throws FileNotFoundException{
		File file = new File(location);
		if(!file.exists()) {
			throw new FileNotFoundException("The address is not correct");
		}
	}
	//method to get all the data from the track
	public static String getStack(String location) {
		Track track = new Track();
			try {
				track.readFile(location);
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
			} catch (GPSException e1) {
				System.out.println(e1.getMessage());
			}
			int size = track.size();
			Point lowest = null,highest = null;
			double totalDistance=0,averageSpeed=0;
			//get all the data needed from the track
			try {
				lowest = track.lowestPoint();
				highest = track.highestPoint();
				totalDistance = track.totalDistance();
				averageSpeed = track.averageSpeed();
			} catch(GPSException e) {
				System.out.println(e.getMessage());
				System.exit(1);
			}
			//set the string needed
			DecimalFormat decimalDigit4 = new DecimalFormat("#0.00000");
			DecimalFormat decimalDigit3 = new DecimalFormat("#0.000");
			String sizeStr= size+"points in track\n";
			String lowestStr = "Lowest point is ("+decimalDigit4.format(lowest.getLongitude())+", "+decimalDigit4.format(lowest.getLatitude())+"), "+lowest.getElevation()+" m\n";
			String highestStr = "Highest point is ("+decimalDigit4.format(highest.getLongitude())+", "+decimalDigit4.format(highest.getLatitude())+"), "+highest.getElevation()+" m\n";
			String totalStr = "Total distance = "+decimalDigit3.format(totalDistance/1000)+" km\n";
			String averageStr = "Average speed = "+decimalDigit3.format(averageSpeed)+" m/s\n";
			track.writeKML("walk.kml");
			return sizeStr+lowestStr+highestStr+totalStr+averageStr;
		
	}
	public static void main(String[] args) {
		if(args.length==0) {
			System.out.println("No input");
			System.exit(1);
		}
		try {
			checkFile(args[0]);
		}
		catch(FileNotFoundException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		System.out.println(getStack(args[0]));
	}

}
