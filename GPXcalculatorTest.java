package edu.upenn.cis350.gpx;
import java.util.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class GPXcalculatorTest {
	GPXtrkpt normal_point1;
	GPXtrkpt normal_point2;
	GPXtrkpt normal_point3;
	
	GPXtrkpt null_point;
	GPXtrkpt boundary_point1;
	GPXtrkpt boundary_point2;
	GPXtrkpt invalid_point1;
	GPXtrkpt invalid_point2;
	GPXtrkpt invalid_point3;
	GPXtrkpt invalid_point4;
	
	
	public void setUp() throws Exception {
		
		normal_point1 = new GPXtrkpt(10,10,null);
		normal_point2 = new GPXtrkpt(-10,10,null);
		normal_point3 = new GPXtrkpt(-10,-10,null);
	
		null_point = null;
		boundary_point1 = new GPXtrkpt(-90,-180,null);
		boundary_point2 = new GPXtrkpt(90,180,null);
		invalid_point1 = new GPXtrkpt(-100,50,null);
		invalid_point2 = new GPXtrkpt(30,-200,null);
		invalid_point3 = new GPXtrkpt(200,100,null);
		invalid_point4 = new GPXtrkpt(50,181, null);
		
	}
	@Test
	//Checks the null GPXtrk case - should be -1
	public void testNull_GPXtrk(){
		GPXtrk trk = null;
		assertTrue("Distance should be -1",GPXcalculator.calculateDistanceTraveled(trk) == -1);
	}
	
	@Test
	//Checks the empty GPXtrk case - should be -1
	public void testEmpty_GPXtrk(){
		GPXtrk trk = new GPXtrk("empty",null);
		assertTrue("Distance should be -1",GPXcalculator.calculateDistanceTraveled(trk) == -1);
	}
	
	@Test
	//Checks GPXtrk with one null GPXtrkseg - should be 0
	public void testNull_GPXtrkseg(){
		GPXtrkseg null_seg = null;
		ArrayList<GPXtrkseg> s = new ArrayList<GPXtrkseg>();
		s.add(null_seg);
		GPXtrk trk = new GPXtrk("empty seg",s);
		assertTrue("Distance should be 0",GPXcalculator.calculateDistanceTraveled(trk) == 0);
	}
	
	@Test
	//Checks GPXtrk with one empty GPXtrkseg - should be 0
	public void testEmpty_GPXtrkseg(){
		GPXtrkseg empty_seg = new GPXtrkseg(null);
		ArrayList<GPXtrkseg> s = new ArrayList<GPXtrkseg>();
		s.add(empty_seg);
		GPXtrk trk = new GPXtrk("empty seg",s);
		assertTrue("Distance should be 0",GPXcalculator.calculateDistanceTraveled(trk) == 0);
	}
	
	@Test
	//Checks GPXtrk with one GPXtrkseg with one GPXtrkpoint - should be 0
	public void testOnePoint(){
		ArrayList<GPXtrkpt> p = new ArrayList<GPXtrkpt>();
		p.add(normal_point1);
		GPXtrkseg one_point_seg = new GPXtrkseg(p);
		ArrayList<GPXtrkseg> t = new ArrayList<GPXtrkseg>();
		t.add(one_point_seg);
		GPXtrk trk = new GPXtrk("one point seg",t);
		assertTrue("Distance should be 0",GPXcalculator.calculateDistanceTraveled(trk) == 0);
	}
	
	@Test
	//Checks GPXtrk with one GPXtrkseg with a null GPXtrkpt = should be 0
	public void testNullPoint(){
		ArrayList<GPXtrkpt> p = new ArrayList<GPXtrkpt>();
		p.add(normal_point1);
		p.add(null_point);
		p.add(normal_point2);
		GPXtrkseg seg = new GPXtrkseg(p);
		ArrayList<GPXtrkseg> t = new ArrayList<GPXtrkseg>();
		t.add(seg);
		GPXtrk trk = new GPXtrk("null point seg",t);
		assertTrue("Distance should be 0",GPXcalculator.calculateDistanceTraveled(trk) == 0);
				
	}
	
	@Test
	//Checks boundary points in both latitude and longitude - should compute to 402.5
	public void testBoundaryPoints(){
		
		ArrayList<GPXtrkpt> p = new ArrayList<GPXtrkpt>();
		p.add(boundary_point1);
		p.add(boundary_point2);
		GPXtrkseg boundary_point_seg = new GPXtrkseg(p);
		ArrayList<GPXtrkseg> t = new ArrayList<GPXtrkseg>();
		t.add(boundary_point_seg);
		GPXtrk trk = new GPXtrk("boundary point seg",t);
		assertTrue("Distance should be 402.5",GPXcalculator.calculateDistanceTraveled(trk) == Math.sqrt(180*180+360*360));
		
		
	}
	
	@Test
	//Checks points with invalid Latitude points - should be 0
	public void testInvalidLatitudePoints(){
		ArrayList<GPXtrkpt> p = new ArrayList<GPXtrkpt>();
		p.add(normal_point1);
		p.add(invalid_point1);
		p.add(normal_point2);
		
		ArrayList<GPXtrkpt> r = new ArrayList<GPXtrkpt>();
		r.add(normal_point2);
		r.add(invalid_point3);
		
		GPXtrkseg seg1 = new GPXtrkseg(p);
		GPXtrkseg seg2 = new GPXtrkseg(r);
		ArrayList<GPXtrkseg> t = new ArrayList<GPXtrkseg>();
		t.add(seg1);
		t.add(seg2);
	
		GPXtrk trk = new GPXtrk("invalid point segs",t);
		assertTrue("Distance should be 0",GPXcalculator.calculateDistanceTraveled(trk) == 0);
		
		
	}
	
	@Test
	//Checks points with invalide Longitude Points - should be 0
	public void testInvalidLongitudePoints(){
		
		ArrayList<GPXtrkpt> q = new ArrayList<GPXtrkpt>();
		q.add(normal_point3);
		q.add(invalid_point2);
		
		ArrayList<GPXtrkpt> s = new ArrayList<GPXtrkpt>();
		s.add(invalid_point4);
		s.add(normal_point3);
		s.add(normal_point1);
				
		GPXtrkseg seg1 = new GPXtrkseg(q);
		GPXtrkseg seg2 = new GPXtrkseg(s);
	
		ArrayList<GPXtrkseg> t = new ArrayList<GPXtrkseg>();
		t.add(seg1);
		t.add(seg2);
		
		GPXtrk trk = new GPXtrk("invalid point segs",t);
		assertTrue("Distance should be 0",GPXcalculator.calculateDistanceTraveled(trk) == 0);
		
	}
	
	@Test
	//Checks the normal case - two segments in the trk - should be 60
	public void testNormal(){
		ArrayList<GPXtrkpt> seg1 = new ArrayList<GPXtrkpt>();
		seg1.add(normal_point1);
		seg1.add(normal_point2);
		seg1.add(normal_point3);
		GPXtrkseg normal_seg1 = new GPXtrkseg(seg1);
		ArrayList<GPXtrkseg> s = new ArrayList<GPXtrkseg>();
		s.add(normal_seg1);
		
		ArrayList<GPXtrkpt> seg2 = new ArrayList<GPXtrkpt>();
		seg2.add(normal_point3);
		seg2.add(normal_point1);
		GPXtrkseg normal_seg2 = new GPXtrkseg(seg2);
		s.add(normal_seg2);
		GPXtrk trk = new GPXtrk("normal trk",s);
		assertTrue("Distance should be 60",GPXcalculator.calculateDistanceTraveled(trk) == 60);
		
	}

}
