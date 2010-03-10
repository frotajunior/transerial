   /*
    2    * Copyright 1994-1995 Sun Microsystems, Inc.  All Rights Reserved.
    3    * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
    4    *
    5    * This code is free software; you can redistribute it and/or modify it
    6    * under the terms of the GNU General Public License version 2 only, as
    7    * published by the Free Software Foundation.  Sun designates this
    8    * particular file as subject to the "Classpath" exception as provided
    9    * by Sun in the LICENSE file that accompanied this code.
   10    *
   11    * This code is distributed in the hope that it will be useful, but WITHOUT
   12    * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
   13    * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
   14    * version 2 for more details (a copy is included in the LICENSE file that
   15    * accompanied this code).
   16    *
   17    * You should have received a copy of the GNU General Public License version
   18    * 2 along with this work; if not, write to the Free Software Foundation,
   19    * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
   20    *
   21    * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
   22    * CA 95054 USA or visit www.sun.com if you need additional information or
   23    * have any questions.
   24    */
      
package util;
      
      /**
       * The CRC-16 class calculates a 16 bit cyclic redundancy check of a set
       * of bytes. This error detecting code is used to determine if bit rot
       * has occured in a byte stream.
       */
      
public class CRC16 {
      
      /** value contains the currently computed CRC, set it to 0 initally */
      public int value;
      
      public CRC16() {
          value = 0;
      }
  
      /** update CRC with byte b */
      public void update(byte aByte) {
          int a, b;
  
          a = (int) aByte;
          for (int count = 7; count >=0; count--) {
              a = a << 1;
              b = (a >>> 8) & 1;
              if ((value & 0x8000) != 0) {
                  value = ((value << 1) + b) ^ 0x1021;
              } else {
                  value = (value << 1) + b;
              }
          }
          value = value & 0xffff;
          return;
      }
  
      /** reset CRC value to 0 */
      public void reset() {
          value = 0;
      }
 }