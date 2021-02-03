import cv2
import os
import sys
import shutil
import subprocess
import numpy as np
import imutils
import random
from PIL import Image
original_path = sys.argv[1]
print(original_path)
i=0
for filename in os.listdir(original_path):
    image_path = original_path+"\\"+str(filename)
    print(str(image_path))
    image = cv2.imread(image_path)
    for x in range(5):
        ratio = random.uniform(0.4,1.5)
        new_image = cv2.resize(image,(0,0),fx=ratio, fy=ratio)
        cv2.imwrite(original_path+"\\"+str(filename[0])+"-"+str(i)+".jpg",new_image)
        i = i+1
        rotated = imutils.rotate(image, random.randint(0,61))
        cv2.imwrite(original_path+"\\"+str(filename[0])+"-"+str(i)+".jpg",rotated)
        i = i+1
        rotated = imutils.rotate(image, random.randint(299,360))
        cv2.imwrite(original_path+"\\"+str(filename[0])+"-"+str(i)+".jpg",rotated)
        i = i+1
    i = i+1
    img = Image.open(image_path).transpose(Image.FLIP_LEFT_RIGHT)
    img.save(original_path+"\\"+str(filename)+"-"+str(i)+".jpg", 'JPEG')
