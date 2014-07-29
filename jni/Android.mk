# Copyright (C) 2009 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := de_reiss_nomb_controller_nativecode_FileHandlingActivity
LOCAL_SRC_FILES := de_reiss_nomb_controller_nativecode_FileHandlingActivity.c

LOCAL_C_INCLUDES := $(LOCAL_PATH)

LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)



include $(CLEAR_VARS)

LOCAL_MODULE    := de_reiss_nomb_controller_nativecode_MessageActivity
LOCAL_SRC_FILES := de_reiss_nomb_controller_nativecode_MessageActivity.c

LOCAL_C_INCLUDES := $(LOCAL_PATH)

LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)



include $(CLEAR_VARS)

LOCAL_MODULE    := de_reiss_nomb_controller_nativecode_PackageManagerInfoActivity
LOCAL_SRC_FILES := de_reiss_nomb_controller_nativecode_PackageManagerInfoActivity.c

LOCAL_C_INCLUDES := $(LOCAL_PATH)

LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)





include $(CLEAR_VARS)

LOCAL_MODULE    := de_reiss_nomb_controller_nativecode_NetworkActivity
LOCAL_SRC_FILES := de_reiss_nomb_controller_nativecode_NetworkActivity.c

LOCAL_C_INCLUDES := $(LOCAL_PATH)

LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)





include $(CLEAR_VARS)

LOCAL_MODULE    := de_reiss_nomb_controller_nativecode_RunCommandActivity
LOCAL_SRC_FILES := de_reiss_nomb_controller_nativecode_RunCommandActivity.c

LOCAL_C_INCLUDES := $(LOCAL_PATH)

LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)






include $(CLEAR_VARS)

LOCAL_MODULE   := opensl_example
LOCAL_C_INCLUDES := $(LOCAL_PATH)
LOCAL_CFLAGS := -O3
LOCAL_CPPFLAGS :=$(LOCAL_CFLAGS)
###

LOCAL_SRC_FILES := opensl_example.c \
opensl_io.c \
java_interface_wrap.cpp

LOCAL_LDLIBS := -llog -lOpenSLES

include $(BUILD_SHARED_LIBRARY)