cmake_minimum_required(VERSION 3.6)

# set the project name
project(glfwapi)

# specify the C++ standard

# add the include dirs
find_package(JNI)
if (JNI_FOUND)
    include_directories(SYSTEM ${JAVA_INCLUDE_PATH})
    include_directories(SYSTEM ${JAVA_INCLUDE_PATH2})
endif()
include_directories("${PROJECT_SOURCE_DIR}/../common")
include_directories("${PROJECT_SOURCE_DIR}/../common/GLFW")
include_directories("${PROJECT_SOURCE_DIR}/../common/KHR")

link_directories("${PROJECT_SOURCE_DIR}/libs")

add_library(wrapper SHARED glfwwrapper.cpp openglwrapper.cpp)

target_link_libraries(wrapper PUBLIC opengl32.dll)
target_link_libraries(wrapper PUBLIC glfw3.dll)