import java.time.ZonedDateTime;

import static java.lang.Math.*;

import java.text.DecimalFormat;

/**
 * Represents a point in space and time, recorded by a GPS sensor.
 *
 * @author Nick Efford & YOUR NAME
 */
public class Point {
  // Constants useful for bounds checking, etc

  private static final double MIN_LONGITUDE = -180.0;
  private static final double MAX_LONGITUDE = 180.0;
  private static final double MIN_LATITUDE = -90.0;
  private static final double MAX_LATITUDE = 90.0;
  private static final double MEAN_EARTH_RADIUS = 6.371009e+6;

  // TODO: Define fields for time, longitude, latitude and elevation
  ZonedDateTime timestamp = ZonedDateTime.now();
  double longitude,latitude,elevation;
  // TODO: Define a constructor
  Point(ZonedDateTime newTime,double newLongitude,double newLatitude,double newElevation)throws GPSException{
	  timestamp = newTime;
	  longitude = newLongitude;
	  latitude = newLatitude;
	  elevation = newElevation;
	  //beneath the comment is the part to determine when to throw the exception
	  if(longitude>MAX_LONGITUDE) {
		  throw new GPSException("LongitudeTooHigh");
	  }
	  if(longitude<MIN_LONGITUDE) {
		  throw new GPSException("LongitudeTooLow");
	  }
	  if(latitude>MAX_LATITUDE) {
		  throw new GPSException("LatitudeTooHigh");
	  }
	  if(latitude<MIN_LATITUDE) {
		  throw new GPSException("LatitudeTooLow");
	  }
	  if(elevation>MEAN_EARTH_RADIUS) {
		  throw new GPSException("ElevationTooHigh");
	  }
  }
  // TODO: Define getters for the fields
  ZonedDateTime getTime() {
	  return timestamp;
  }
  double getLongitude(){
	  return longitude;
  }
  double getLatitude(){
	  return latitude;
  }
  double getElevation() {
	  return elevation;
  }
  // TODO: Define a toString() method that meets requirements
  public String toString() {
	  //here is the part to leave five decimal digits
	  DecimalFormat dlg = new DecimalFormat("#.00000");
	  String slg = dlg.format(longitude);
	  String slt = dlg.format(latitude);
	  return ("("+slg+", "+slt+"), "+elevation+" m");
  }
  // Do not alter anything beneath this comment

  /**
   * Computes the great-circle distance or orthodromic distance between
   * two points on a spherical surface, using Vincenty's formula.
   *
   * @param p First point
   * @param q Second point
   * @return Distance between the points, in metres
   */
  public static double greatCircleDistance(Point p, Point q) {
    double phi1 = toRadians(p.getLatitude());
    double phi2 = toRadians(q.getLatitude());

    double lambda1 = toRadians(p.getLongitude());
    double lambda2 = toRadians(q.getLongitude());
    double delta = abs(lambda1 - lambda2);

    double firstTerm = cos(phi2)*sin(delta);
    double secondTerm = cos(phi1)*sin(phi2) - sin(phi1)*cos(phi2)*cos(delta);
    double top = sqrt(firstTerm*firstTerm + secondTerm*secondTerm);

    double bottom = sin(phi1)*sin(phi2) + cos(phi1)*cos(phi2)*cos(delta);

    return MEAN_EARTH_RADIUS * atan2(top, bottom);
  }
}
