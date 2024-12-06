cmake_minimum_required(VERSION 3.14)
project(runner LANGUAGES CXX)
set(path_cxx "C:/Users/hua/libs/LLVM/bin")
set(CXX_STANDARD 21)
set(CMAKE_CXX_COMPILER ${path_cxx}/clang++.exe)
set(CMAKE_C_COMPILER ${path_cxx}/clang.exe)

# set(CMAKE_GENERATOR Ninja)
set(CMAKE_EXPORT_COMPILE_COMMANDS ON)
# Define the application target. To change its name, change BINARY_NAME in the
# top-level CMakeLists.txt, not the value here, or `flutter run` will no longer
# work.
#
# Any new source files that you add to the application should be added here.
# find_package(zxing PATHS "./zxing-cpp-2.2.1")
# execute_process("./gradlew :jar")
# execute_process("native-image -H:+UnlockExperimentalVMOptions -H:-CheckToolchain -H:+AddAllCharsets  --shared -o libdoc com.hua.graalvm.SimpleDemo -cp ./genki-all.jar")

set(JAVA_OUTPUT_DIR java/build)
set(JAR_FILE ${OUTPUT_DIR}/libs/genki-all.jar)
message(${JAR_FILE})
add_custom_command(
    OUTPUT ${JAR_FILE}
    COMMAND ./gradlew :jar
    WORKING_DIRECTORY java
    COMMENT "build jar"
)
set(NATIVE_LIB graalvm/libdoc.dll)

# add_custom_target(CleanOutputs
#     COMMAND ${CMAKE_COMMAND} -E remove -f ${JAR_FILE} ${NATIVE_LIB}
#     COMMAND ${CMAKE_COMMAND} -E echo "Cleaned previous build outputs: ${JAR_FILE}, ${NATIVE_LIB}."
#     COMMENT "Cleaning previous build outputs..."
# )
message(${NATIVE_LIB})
add_custom_command(
    OUTPUT ${NATIVE_LIB}
    COMMAND native-image --no-fallback --strip-debug --noconsole -H:+UnlockExperimentalVMOptions -H:-CheckToolchain -H:+AddAllCharsets  --shared -o libdoc com.hua.graalvm.SimpleDemo -cp ${JAR_FILE}
    WORKING_DIRECTORY graalvm
    DEPENDS ${JAR_FILE} 
    COMMENT "Generating native shared library with GraalVM"
)


set(BINARY_NAME read_doc)
add_executable(${BINARY_NAME} "main.cpp") 

set(LIBRARY_NAME "docwarpper")
add_library(${LIBRARY_NAME} SHARED "main.cpp")
target_include_directories(${BINARY_NAME} PRIVATE "graalvm")
target_link_directories(${BINARY_NAME} PRIVATE "graalvm")
target_link_libraries(${BINARY_NAME} "doc")

target_include_directories(${LIBRARY_NAME} PRIVATE "graalvm")
target_link_directories(${LIBRARY_NAME} PRIVATE "graalvm")
target_link_libraries(${LIBRARY_NAME} "doc")
# target_include_directories(${BINARY_NAME} )
# target_include_directories(${BINARY_NAME} )

