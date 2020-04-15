all : elf_prog
	@echo "编译完成"
	
elf_prog : elf.o
	${CC} elf.o -o elf_prog

elf.o : elf.h elf.c
	${CC} -c -o elf.o elf.c 