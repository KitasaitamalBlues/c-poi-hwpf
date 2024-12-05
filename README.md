# 第一步

native-image  -H:+UnlockExperimentalVMOptions -H:-CheckToolchain -H:+AddAllCharsets  --shared -o libdoc com.hua.graalvm.SimpleDemo -cp libs/doc-all.jar --no-fallback --strip-debug --noconsole

拷贝以上所得产物到根目录下 graalvm

# 第二步

mkdir build && cd build
cmake -G "Ninja" ..
cmake --build .