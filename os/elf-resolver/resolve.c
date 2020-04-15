#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>
#include "elf_format.h"

void handleStrTable(int fd, Elf64_Shdr * header, char ** string_table, int index);

int main(int argc, char const *argv[])
{
    /* code */
    Elf64_Ehdr elf_header;
    Elf64_Phdr prog_header;
    Elf64_Shdr section_header;
    int fd;
    int count = 0;
    Elf64_Off hdr_offset;

    if (argc < 2)
    {
        printf("未传入需要解析的文件名\n");
        return 1;
    }
    
    fd = open(argv[1], O_RDONLY);
    read(fd, &elf_header, sizeof(Elf64_Ehdr));
    // 验证 magic number
    if (strcmp("\177ELF\2\1\1", elf_header.e_ident) == 0)
    {
        printf("this is a ELF file\n");
    }
    // 验证文件类型
    switch (elf_header.e_type)
    {
    case ET_EXEC:
        printf("this is a excutable file\n");
        break;
    case ET_REL:
        printf("this is a relocation file\n");
        break;
    case ET_NONE:
        printf("this is a unkownn file\n");
        break;
    default:
        break;
    }

    // 验证适应的体系结构
    switch (elf_header.e_machine)
    {
    case EM_386:
        printf("this is a inter 80386 machine\n");
        break;
    case EM_860:
        printf("this is a inter 8086 machine\n");
        break;
    case EM_NONE:
        printf("this is a unkownn machine\n");
        break;
    case EM_X86_64:
        printf("this is a X86_64 machine\n");
        break;
    default:
        break;
    }

    printf("program entry point is 0x%x\n", elf_header.e_entry);

    // 程序头数量
    printf("program header number : %d\n", elf_header.e_phnum);
    // 程序头偏移
    hdr_offset = elf_header.e_phoff;
    while (count <= elf_header.e_phnum)
    {
        /* code */
        lseek(fd, hdr_offset, SEEK_SET);
        read(fd, &prog_header, elf_header.e_phentsize);
        printf("v_addr = 0x%x, p_addr = 0x%x, file offset = 0x%x, alignment, file & memory = 0x%x\n", 
            prog_header.p_vaddr, prog_header.p_paddr, prog_header.p_offset, prog_header.p_align);
        count++;
        hdr_offset += elf_header.e_phentsize;
    }

    // section 数量
    printf("section header number : %d\n", elf_header.e_shnum);
    hdr_offset = elf_header.e_shoff;
    count = 0;

    int string_table_count = 0;
    char * string_table[1024];
    int index = 0;

    while (count <= elf_header.e_shnum)
    {
        /* code */
        lseek(fd, hdr_offset, SEEK_SET);
        read(fd, &section_header, elf_header.e_shentsize);

        switch (section_header.sh_type)
        {
        case SHT_STRTAB:
            printf("SHT_STRTAB section\n");
            handleStrTable(fd, &section_header, string_table, index);
            index++;
            break;
        default:
            break;
        }
        printf("[%d] sh_addr = 0x%x, sh_offset = 0x%x, sh_size = 0x%x, name index = %d\n",
             count, section_header.sh_addr, section_header.sh_offset, section_header.sh_size, section_header.sh_name);

        count++;
        hdr_offset += elf_header.e_shentsize;
    }

    hdr_offset = elf_header.e_shoff;   
    count = 0;
    while (count <= elf_header.e_shnum){
        lseek(fd, hdr_offset, SEEK_SET);
        read(fd, &section_header, elf_header.e_shentsize);
        printf("section name index : %d\n", section_header.sh_name);
        printf("section name : %s\n", string_table[1] + section_header.sh_name);
        count++;
        hdr_offset += elf_header.e_shentsize;
    }
    
    return 0;
}

void handleStrTable(int fd, Elf64_Shdr * header, char ** string_table, int index){
    char * buf = malloc(header->sh_size);
    lseek(fd, header->sh_offset, SEEK_SET);
    read(fd, buf, header->sh_size);
    string_table[index] = buf;

    index = 0;
    int offset = 1;
    while (offset < header->sh_size)
    {
        /* code */
        printf("index : %d, name : %s\n", index, buf + offset);
        index++;
        offset += strlen(buf + offset) + 1;
    }
    
}