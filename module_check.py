a=0
try:
    import argparse
except ImportError:
    print("argparse\n")
    a = a+1
try:
    import collections
except ImportError:
    print("collections\n")
    a = a+1
try:
    import hashlib
except ImportError:
    print("hashlib\n")
    a = a+1
try:
    import random
except ImportError:
    print("random\n")
    a = a+1
try:
    import re
except ImportError:
    print("re\n")
    a = a+1
try:
    import numpy
except ImportError:
    print("numpy\n")
    a = a+1
try:
    import tensorflow
except ImportError:
    print("tensorflow\n")
    a = a+1
try:
    import tensorflow_hub
except ImportError:
    print("tensorflow_hub\n")
    a = a+1
try:
    import cv2
except ImportError:
    print("cv2\n")
    a = a+1
try:
    import shutil
except ImportError:
    print("shutil\n")
    a = a+1
try:
    import imutils
except ImportError:
    print("imutils\n")
    a = a+1
if a==0:
    print(a)
