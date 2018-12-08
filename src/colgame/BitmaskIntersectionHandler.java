package colgame;

public class BitmaskIntersectionHandler {
   private static int overlayTopLeftCornerX, overlayTopLeftCornerY, widthOfOverlap, heightOfOverlap, b1CoordX, b1CoordY, b2CoordX, b2CoordY;
   private static int b1StartOfWidthIteration,   b1StartOfHeightIteration;
   private static int b2StartOfWidthIteration,   b2StartOfHeightIteration;
   private static Bitmask b1, b2;
   
   public static boolean checkForIntersectionWithBitmask(Entity e1, Entity e2) {
      b1 = e1.getBitmask();
      b2 = e2.getBitmask();
      init();
      getOverlappingLeftTopCornerAndCalcOffset();
      widthOfOverlap = calcIntersectionWidth();
      heightOfOverlap = calcIntersectionHeight();
      return checkOverlappingAreaForIntersection();
   }

   private static void init() {
      b1CoordX = (int)b1.getCoordinates().getX();
      b1CoordY = (int)b1.getCoordinates().getY();
      b2CoordX = (int)b2.getCoordinates().getX();
      b2CoordY = (int)b2.getCoordinates().getY();
      b1StartOfWidthIteration = 0;
      b1StartOfHeightIteration = 0;
      b2StartOfWidthIteration = 0;
      b2StartOfHeightIteration = 0;
   }

   private static void getOverlappingLeftTopCornerAndCalcOffset() {
      if(b1CoordX > b2CoordX) {
         overlayTopLeftCornerX = b1CoordX;
         b2StartOfWidthIteration = Math.abs(b2CoordX - b1CoordX);
      } else {
         overlayTopLeftCornerX = b2CoordX;
         b1StartOfWidthIteration = Math.abs(b2CoordX - b1CoordX);
      }

      if(b1CoordY > b2CoordY) {
         overlayTopLeftCornerY = b1CoordY;
         b2StartOfHeightIteration = Math.abs(b2CoordY - b1CoordY);
      } else {
         overlayTopLeftCornerY = b2CoordY;
         b1StartOfHeightIteration = Math.abs(b2CoordY - b1CoordY);
      }
   }

   private static int calcIntersectionWidth() {
      int width1 = b1CoordX + b1.getWidth();
      int width2 = b2CoordX + b2.getWidth();
      return width1 < width2 ? width1 : width2;
   }

   private static int calcIntersectionHeight() {
      int height1 = b1CoordY + b1.getHeight();
      int height2 = b2CoordY + b2.getHeight();
      return height1 < height2 ? height1 : height2;
   }

   private static boolean checkOverlappingAreaForIntersection() {
      for(int i = overlayTopLeftCornerX, i1 = b1StartOfWidthIteration, i2 = b2StartOfWidthIteration; i < widthOfOverlap; i++, i1++, i2++) {
         for(int j = overlayTopLeftCornerY, j1 = b1StartOfHeightIteration, j2 = b2StartOfHeightIteration; j < heightOfOverlap; j++, j1++, j2++) {
            if(b1.getCollisionPixel()[i1][j1]
                  && b2.getCollisionPixel()[i2][j2]) {
               return true;
            }
         }
      }
      return false;
   }
}