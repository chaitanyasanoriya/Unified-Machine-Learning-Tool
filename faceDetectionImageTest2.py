import cv2
import os
import sys
import shutil
import subprocess
face_cascade = cv2.CascadeClassifier('Face_cascade.xml')
original_path = sys.argv[1]
print(original_path)
#original_path = "D:\College Stuff\Machine Learning\Tests\Minor Project\Test 1 - without ED\data\Anne"
#new_path = original_path + "1"
#os.rename(original_path,new_path)
i=0
#os.mkdir(original_path)
for filename in os.listdir(original_path):
    image_path = original_path+"\\"+str(filename)
    print(str(image_path))
    image = cv2.imread(image_path)
    height, width, channels = image.shape
    max_value = max(height,width)
    if max_value>1000 :
        ratio = 720/max_value
        image = cv2.resize(image,(0,0),fx=ratio, fy=ratio)
    gray = cv2.cvtColor(image,cv2.COLOR_BGR2GRAY)
    faces = face_cascade.detectMultiScale(gray, scaleFactor=1.16,minNeighbors=5,minSize=(25,25),flags=0)
    for (x, y, w, h) in faces:

        new_start_y = y
        if y-50<0:
            new_start_y = 0
        else :
            new_start_y = y-50;
        new_end_y = y+h;
        if y+h+20>height:
            new_end_y = height
        else :
            new_end_y = y+h+20
        new_start_x = x
        if x-10<0:
            new_start_x = 0
        else :
            new_start_x = x-10
        new_end_x = x+w
        if x+w+10 > width:
            new_end_x = width
        else :
            new_end_x = x+w+10

        #print(new_start_y," ",new_end_y," ",new_start_x," ",new_end_x)

        new_image = image[new_start_y:new_end_y,new_start_x:new_end_x]
        #cv2.imwrite(original_path+"\\"+str(i)+".jpg",new_image)
        cv2.imwrite(original_path+"\\"+str(filename)+"-"+str(i)+".jpg",new_image)
        i = i+1
        #cv2.rectangle(image, (x, y-60), (x+w, y+h+20), (0, 255, 0), 2)
    #cv2.imshow("Faces found", image)
    #time.sleep(3)
#cv2.waitKey(0)
#shutil.rmtree(new_path)
#subprocess.Popen(['python', 'retrain.py', "--image_dir=D:\College Stuff\Machine Learning\Tests\Minor Project\Test 1 - without ED\data"])
