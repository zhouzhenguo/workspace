# 设置建立你的本地库所需的CMake的最低版本。
# 这为了确保某些功能在你构建的时候是可用的。

cmake_minimum_required(VERSION 3.4.1)

# 指定库名
# 并提供源代码的相对路径.
# 你可以通过add.library()命令定义多个库,CMake会去构建他们，
# 当你构建App 的时候，Gradle 会自动把库打包到你的apk 中

add_library( # Specifies the name of the library.
             bsdiffpatch

            # Sets the library as a shared library.
             SHARED

            # Provides a relative path to your source file(s).
            bzip2/blocksort.c
            bzip2/bzip2.c
            bzip2/bzlib.c
            bzip2/compress.c
            bzip2/crctable.c
            bzip2/decompress.c
            bzip2/huffman.c
            bzip2/randtable.c
            bsdiff.c
            bspatch.c
            native.c)