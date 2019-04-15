import java.util.*;
import java.io.*;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class Track {
	Point[] points=new Point[200];//define the field of a sequence of Point objects
	int size=0;//size is the amount of the objects
	Track(){
	}
	void readFile(String location) throws FileNotFoundException,GPSException{
		//To estimate if the location is available
		File file = new File(location);
		if(!file.exists()) {
			throw new FileNotFoundException("Address doesn't exist");
		}
		//Use scanner to get the data from the file
		Scanner scanner = new Scanner(new File(location));
		//Read data line-by-line
		Scanner valuescanner = new Scanner(scanner.nextLine());
		int count = 0,num1=0;//count is used to decide where the data should be put, num1 is to get the total amount of available points
		do{
			valuescanner = new Scanner(scanner.nextLine());
			valuescanner.useDelimiter(",");//split up the line on commas
			ZonedDateTime time=null;
			double longi=0,latit=0,eleva=0;
			while(count<4) {
				String data = valuescanner.next();
				//if data doesn't finish fetching but scanner fetch no data,then there is an exception
				if(count<3&&!valuescanner.hasNext()) {
					throw new GPSException("Missing Data");
				}
				if(count==0) {
					//timetamp is put
					time= ZonedDateTime.parse(data);
					count++;
				}
				else if(count==1) {
					//longitude is put
					longi = Double.parseDouble(data);
					count++;
				}
				else if(count==2) {
					//latitude is put
					latit= Double.parseDouble(data);
					count++;
				}
				else if(count==3) {
					//elevation is put
					eleva= Double.parseDouble(data);
					count++;
				}
			}
			Point res = new Point(time,longi,latit,eleva);//initialize the point to be put into the sequence
			points[num1]=res;
			count=0;
			num1++;
		}while(scanner.hasNextLine()&&num1<=200);
		size=num1;//get the size in advance
		scanner.close();
		valuescanner.close();
	}
	int posit=0;//posit is used to locate where the point should be put
	void add(Point point) {
		points[posit]=point;
		posit++;
		//if method add is applied, size should also change
		size++;
	}
	int size() {
			return size;
	}
	Point get(int position) throws GPSException{
		if(position<0) {
			throw new GPSException("IndexTooLow");//position can't be smaller than zero
		}
		if(position>=size) {
			throw new GPSException("IndexTooHigh");//position is always less than size
		}
		return points[position];
	}
	Point lowestPoint() throws GPSException{
		//if there is just one point or no point, throw the exception
		if(size<2) {
			throw new GPSException("PointNotEnough");
		}
		double min=Double.MAX_VALUE;//let the first elevation input be the smallest
		int position=0,count=0;//position locates the point,count counts the number of times of the loop
		for(Point each : points) {
			if(each!=null) {
				if(each.elevation<min) {
					min=each.elevation;
					position=count;
				}
				count++;
			}
			else {
				break;//if the point is empty, shut down the loop
			}
		}
		return points[position];
	}
	Point highestPoint() throws GPSException{
		//if there is just one point or no point, throw the exception
		if(size<2) {
			throw new GPSException("PointNotEnough");
		}
		double max=0;
		int position=0,count=0;
		for(Point each : points) {
			if(each!=null) {
				if(each.elevation>max) {
					max=each.elevation;
					position=count;
				}
				count++;
			}
			else {
				break;
			}
		}
		return points[position];
		
	}
	double totalDistance() throws GPSException {
		//if there is just one point or no point, throw the exception
		if(size<2) {
			throw new GPSException("PointNotEnough");
		}
		double total=0;
		for(int i=0;i<size-1;i++) {
				double greatCircleDistance = Point.greatCircleDistance(points[i], points[i+1]);//get the greatcircleDistance
				total+=greatCircleDistance;
		}
		return total;
	}
	double averageSpeed() throws GPSException {	
		//if there is just one point or no point, throw the exception	
		if(size<2) {
			throw new GPSException("PointNotEnough");
		}
		double speed=0,total=0;
		total = totalDistance();//get the total distance
		double timeLag = ChronoUnit.SECONDS.between(points[0].timestamp, points[size-1].timestamp);//get the time intervals
		speed = total/timeLag;
		return speed;
	}
	public void writeKML(String filename) {
		try {
			String head = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><kml xmlns=\"http://earth.google.com/kml/2.1\"><Document><name>Pathtest</name><Style id=\"test\"><LineStyle><color>#FF0000</color><width>8</width></LineStyle></Style><Placemark><LineString><extrude>1</extrude><tessellate>1</tessellate><altitudeMode>absolute</altitudeMode><coordinates>";
			String body ="";
			for(Point each : points) {
				if(each!=null) {
					body=body+String.valueOf(each.getLongitude())+","+String.valueOf(each.getLatitude())+","+String.valueOf(each.getElevation())+"\n";
				}
				else {
					break;
				}
			}
			String tail = "</coordinates></LineString></Placemark></Document></kml>";
			File file = new File(filename);
			file.createNewFile();
			FileWriter filew = new FileWriter(file.getName());
			filew.write(head+body+tail);
			filew.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
