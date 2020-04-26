CFLAGS = -std=c99

all : elf_resolver
	@echo "编译完成"
	
elf_resolver : resolver.o
	${CC} $^ -o $@

resolver.o : resolver.c elf_format.h
	${CC} ${CFLAGS} -c -o $@  $<