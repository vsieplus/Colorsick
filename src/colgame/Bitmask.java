package colgame;

import org.newdawn.slick.Image;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;

import java.io.Serializable;
import javax.vecmath.*;

public class Bitmask implements Serializable {
   private static final long serialVersionUID = -5909146496764863208L;
   private boolean collisionPixel[][];
   private int width, height;
   private Vector2f coordinates;

   public Bitmask(Image image, Color nonCollisionColer, Vector2f coordinates) {
      this.createBitmaskFromImage(image, nonCollisionColer);
      this.coordinates = coordinates;
      width = collisionPixel.length;
      height = collisionPixel[0].length;
   }

   public static Bitmask createBitmaskWithTopLeftPixelAsNonCollisionColor(
         Image image, Vector2f coordinates) {
      return createBitmask(image, image.getColor(0, 0), coordinates);
   }

   public static Bitmask createBitmask(Image image, Color nonCollisionColer,
		   Vector2f coordinates){
      return new Bitmask(image, nonCollisionColer, coordinates);
   }

   private void createBitmaskFromImage(Image image, Color nonCollisionColor)
         {
      if(image.getWidth() < 1 || image.getHeight() < 1)
         return;
      collisionPixel = new boolean[image.getWidth()][image.getHeight()];
      for(int i = 0; i < collisionPixel.length; i++) {
         for(int j = 0; j < collisionPixel[0].length; j++) {
            if(image.getColor(i, j).equals(nonCollisionColor)) {
               collisionPixel[i][j] = false;
            } else {
               collisionPixel[i][j] = true;
            }
         }
      }
   }

   public boolean[][] getCollisionPixel() {
      return collisionPixel;
   }

   public void setCollisionPixel(boolean[][] collisionPixel) {
      this.collisionPixel = collisionPixel;
   }

   public Vector2f getCoordinates() {
      return coordinates;
   }

   public void setCoordinates(Vector2f coordinates) {
      this.coordinates = coordinates;
   }

   public int getWidth() {
      return width;
   }

   public int getHeight() {
      return height;
   }

   public String toString() {
      return this.getClass().getName() + "[width=" + collisionPixel.length
            + ", height=" + collisionPixel[0].length + ", coordinates="
            + coordinates + "]";
   }
}