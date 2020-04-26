
all: static-main
	@echo "compile done"

share-main: share-main.o libshared.so
	${CC} -L. $< -lshared -o $@

share-main.o: share-main.c shared.h
	${CC} -c $< -o $@

libshared.so: libshared.so.1
	ln -s $< $@

libshared.so.1: shared.o
	${CC} -shared -Wl,-soname,$@ $< -o $@

shared.o: shared.c shared.h
	${CC} -c -fPIC $<

# static-main.o 引用了 libstatic.a的函数。要放前面
static-main: static-main.o libstatic.a
	${CC} -static -L. $< -lstatic -o $@

static-main.o: static-main.c
	${CC} -c $<

libstatic.a: static.o
	${AR} crs $@ $<

static.o: static.c static.h
	${CC} -c $<

clean : 
	rm -f libshared.so.1 libshared.so shared.o share-main.o share-main static-main.o libstatic.a static.o static-main

excuteShared:
	LD_LIBRARY_PATH=. ./share-main

excuteStatic:
	./static-main

.PHONY: clean all share-main excuteShared excuteStatic