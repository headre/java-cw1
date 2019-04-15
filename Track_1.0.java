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
}
