package com.shhutapp.geo.maparea;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.shhutapp.MainActivity;
import com.shhutapp.geo.maparea.MapAreaWrapper.MarkerMoveResult;
import com.shhutapp.utils.Geo;

public class MapAreaManager implements OnMapLongClickListener{
//public class MapAreaManager implements OnMarkerDragListener, OnMapLongClickListener {

	private static int DEFAULT_FILL_COLOR = 0xff0000ff;
	private static int DEFAULT_STROKE_COLOR = 0xff000000;
	private static int DEFAULT_STROKE_WIDTH = 1;

    private List<MapAreaWrapper> areas = new ArrayList<MapAreaWrapper>(1);
    private GoogleMap map;
    
	private int fillColor = DEFAULT_FILL_COLOR;
	private int strokeWidth = DEFAULT_STROKE_WIDTH;
	private int strokeColor = DEFAULT_STROKE_COLOR;
	
	private int minRadiusMeters = -1;
	private int maxRadiusMeters = -1;
    
	private MapAreaMeasure initRadius;
	
	private CircleManagerListener circleManagerListener;
	
	private int moveDrawableId = -1;
	private int radiusDrawableId = -1;
	
	private float moveDrawableAnchorU;
	private float moveDrawableAnchorV;
	private float resizeDrawableAnchorU;
	private float resizeDrawableAnchorV;

	public interface CircleManagerListener {
		void onCreateCircle(MapAreaWrapper draggableCircle);
		void onResizeCircleEnd(MapAreaWrapper draggableCircle);
		void onMoveCircleEnd(MapAreaWrapper draggableCircle);
		void onMoveCircleStart(MapAreaWrapper draggableCircle);
		void onResizeCircleStart(MapAreaWrapper draggableCircle);
		void onMinRadius(MapAreaWrapper draggableCircle);
		void onMaxRadius(MapAreaWrapper draggableCircle);
	}

	private boolean isFound;

	public MapAreaManager(GoogleMap map, int strokeWidth, int strokeColor, int circleColor,
			int moveDrawableId, int resizeDrawableId, float moveDrawableAnchorU, float moveDrawableAnchorV, float resizeDrawableAnchorU, float resizeDrawableAnchorV,
			MapAreaMeasure initRadius, CircleManagerListener circleManagerListener) {
		
		this.map = map;
		this.circleManagerListener = circleManagerListener;
		this.strokeWidth = strokeWidth;
		this.strokeColor = strokeColor;
		this.fillColor = circleColor;
		this.moveDrawableId = moveDrawableId;
		this.radiusDrawableId = resizeDrawableId;
		this.moveDrawableAnchorU = moveDrawableAnchorU;
		this.moveDrawableAnchorV = moveDrawableAnchorV;
		this.resizeDrawableAnchorU = resizeDrawableAnchorU;
		this.resizeDrawableAnchorV = resizeDrawableAnchorV;
		this.initRadius = initRadius;
		this.isFound = false;
		//map.setOnMarkerDragListener(this);
        map.setOnMapLongClickListener(this);
	}
	public MapAreaManager(GoogleMap map, int strokeWidth, int strokeColor, int circleColor,
			MapAreaMeasure initRadius, CircleManagerListener circleManagerListener) {
		this(map, strokeWidth, strokeColor, circleColor, -1, -1, initRadius, circleManagerListener);
	}
	public MapAreaManager(GoogleMap map, int strokeWidth, int strokeColor, int circleColor,
			int moveDrawableId, int radiusDrawableId, 
			MapAreaMeasure initRadius, CircleManagerListener circleManagerListener) {
		this(map, strokeWidth, strokeColor, circleColor, moveDrawableId, radiusDrawableId, 0.5f, 1f, 0.5f, 1f, initRadius, circleManagerListener);
	}
	public List<MapAreaWrapper> getCircles() {
		return areas;
	}
	public void setMinRadius(int minRadius) {
		this.minRadiusMeters = minRadius;
	}
	public void setMaxRadius(int maxRadius) {
		this.maxRadiusMeters = maxRadius;
	}
/*
	@Override
    public void onMarkerDragStart(Marker marker) {
    	MarkerMoveResultWithCircle result = onMarkerMoved(marker);
        switch (result.markerMoveResult) {
	        case minRadius: {
	        	circleManagerListener.onMinRadius(result.draggableCircle);
	        	break;
	        }
	        case maxRadius: {
	        	circleManagerListener.onMaxRadius(result.draggableCircle);
	        	break;
	        }
	        case radiusChange: {
	        	circleManagerListener.onResizeCircleStart(result.draggableCircle);
	        	break;
	        }
	        case moved: {
    			circleManagerListener.onMoveCircleStart(result.draggableCircle);
	        	break;
	        }
	        default: break;
        }
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
    	MarkerMoveResultWithCircle result = onMarkerMoved(marker);
        switch (result.markerMoveResult) {
	        case minRadius: {
	        	circleManagerListener.onMinRadius(result.draggableCircle);
	        	break;
	        }
	        case maxRadius: {
	        	circleManagerListener.onMaxRadius(result.draggableCircle);
	        	break;
	        }
	        case radiusChange: {
	        	circleManagerListener.onResizeCircleEnd(result.draggableCircle);
	        	break;
	        }
	        case moved: {
    			circleManagerListener.onMoveCircleEnd(result.draggableCircle);
	        	break;
	        }
	        default: break;
        }
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    	MarkerMoveResultWithCircle result = onMarkerMoved(marker);
        switch (result.markerMoveResult) {
	        case minRadius: {
	        	circleManagerListener.onMinRadius(result.draggableCircle);
	        	break;
	        }
	        case maxRadius: {
	        	circleManagerListener.onMaxRadius(result.draggableCircle);
	        	break;
	        }
	        default: break;
        }
    }

    public void add(MapAreaWrapper draggableCircle) {
    	areas.add(draggableCircle);
    }
*/
    private class MarkerMoveResultWithCircle {
    	MarkerMoveResult markerMoveResult;
    	MapAreaWrapper draggableCircle;
    	public MarkerMoveResultWithCircle(MarkerMoveResult markerMoveResult, MapAreaWrapper draggableCircle) {
    		this.markerMoveResult = markerMoveResult;
    		this.draggableCircle = draggableCircle;
    	}
	}
    private MarkerMoveResultWithCircle onMarkerMoved(Marker marker) {
    	MarkerMoveResult result = MarkerMoveResult.none;
    	MapAreaWrapper affectedDraggableCircle = null;

    	for (MapAreaWrapper draggableCircle : areas) {
        	result = draggableCircle.onMarkerMoved(marker); 
            if (result != MarkerMoveResult.none) {
            	affectedDraggableCircle = draggableCircle;
                break;
            }
        }
    	return new MarkerMoveResultWithCircle(result, affectedDraggableCircle);
    }
    @Override
    public void onMapLongClick(LatLng point) {
    	double initRadiusMetersFinal;
    	if (initRadius.unit == MapAreaMeasure.Unit.meters) {
    		initRadiusMetersFinal = initRadius.value;
    	} else {
    		Point screenCenterPoint = map.getProjection().toScreenLocation(point);
    		LatLng radiusLatLng = map.getProjection().fromScreenLocation(new Point(screenCenterPoint.x + (int)initRadius.value, screenCenterPoint.y));
    		initRadiusMetersFinal = MapAreasUtils.toRadiusMeters(point, radiusLatLng);
    	}
        MapAreaWrapper circle = new MapAreaWrapper(map, point, initRadiusMetersFinal, "Name",strokeWidth, strokeColor, fillColor, minRadiusMeters, maxRadiusMeters,
        		moveDrawableId, radiusDrawableId, moveDrawableAnchorU, moveDrawableAnchorV, resizeDrawableAnchorU, resizeDrawableAnchorV, "Address", MainActivity.getMainActivity());
        areas.add(circle);
        circleManagerListener.onCreateCircle(circle);
		isFound = (areas.size()>0);
    }
	public void setFound(boolean is){
		isFound = is;
	}
	public boolean isFound(){
		return isFound;
	}
	public MapAreaWrapper inArea(LatLng geopoint) {
		for(MapAreaWrapper wr : areas){
			if(Geo.distance(wr.getCenter(),geopoint)<wr.getRadius()) return wr;
		}
		return null;
	}
	public void delete(MapAreaWrapper wr){
		areas.remove(wr);
		wr.remove();
		isFound = (areas.size()>0);
	}
}
