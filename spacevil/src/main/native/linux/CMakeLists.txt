cmake_minimum_required(VERSION 3.10)

# set the project name
project(glfwapi)

cmake_policy (SET CMP0072 NEW)
find_package(OpenGL REQUIRED)

# specify the C++ standard
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED True)

# add the include dirs
set(JAVA_AWT_LIBRARY NotNeeded)
set(JAVA_JVM_LIBRARY NotNeeded)
set(JAVA_AWT_INCLUDE_PATH NotNeeded)
find_package(JNI REQUIRED)
if (JNI_FOUND)
    include_directories(SYSTEM ${JAVA_INCLUDE_PATH})
    include_directories(SYSTEM ${JAVA_INCLUDE_PATH2})
endif()
include_directories("${PROJECT_SOURCE_DIR}/../common")
include_directories("${PROJECT_SOURCE_DIR}/../common/GLFW")
include_directories("${PROJECT_SOURCE_DIR}/../common/KHR")

link_directories("${PROJECT_SOURCE_DIR}/../../resources/native/linux/")

add_library(wrapper SHARED glfwwrapper.cpp openglwrapper.cpp)

target_link_libraries(wrapper PUBLIC ${OPENGL_LIBRARIES} libglfw3.so)
