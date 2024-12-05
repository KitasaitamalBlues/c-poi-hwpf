# 第一步

native-image --no-fallback --strip-debug --noconsole -H:+UnlockExperimentalVMOptions -H:-CheckToolchain -H:+AddAllCharsets  --shared -o libdoc com.hua.graalvm.SimpleDemo -cp libs/doc-all.jar

拷贝以上所得产物到根目录下 graalvm

# 第二步

mkdir build && cd build
cmake -G "Ninja" ..
cmake --build .