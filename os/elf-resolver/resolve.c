#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>
#include "elf_format.h"

// 字符串表的个数
#define TABLE_ENTRY_COUNT 1024

void resolveStrTable(int fd, Elf64_Shdr * header, char ** string_table, int index);
void resolveMagicNo(Elf64_Ehdr *elf_header);
void resolveFileType(Elf64_Ehdr *elf_header);
void resolveArchitecture(Elf64_Ehdr *elf_header);
void traveralProgramHeader(int fd, Elf64_Ehdr * elf_header, Elf64_Phdr * prog_header);
void traveralSectionHeader(int fd, Elf64_Ehdr * elf_header, Elf64_Shdr * section_header, char ** string_table, int * section_table_name_ref);
void displaySectionNames(int fd, Elf64_Ehdr * elf_header, Elf64_Shdr * section_header, char ** string_table, int section_table_name_ref);
void resolvePromgramHeaderType(Elf64_Phdr * prog_header, int count);

int main(int argc, char const *argv[])
{
    /* code */
    Elf64_Ehdr elf_header;
    Elf64_Phdr prog_header;
    Elf64_Shdr section_header;
    int fd;
    // 字符串表 的集合
    char * string_table[TABLE_ENTRY_COUNT];
    // section table的名字 所在的 字符串表 的 string_table 的索引
    int section_table_name_ref;

    if (argc < 2)
    {
        printf("未传入需要解析的文件名\n");
        return 1;
    }
    
    fd = open(argv[1], O_RDONLY);
    read(fd, &elf_header, sizeof(Elf64_Ehdr));
    // 验证 magic number
    resolveMagicNo(&elf_header);
    // 验证文件类型
    resolveFileType(&elf_header);
    // 验证适应的体系结构
    resolveArchitecture(&elf_header);

    printf("program entry point is 0x%x\n", elf_header.e_entry);

    // 程序头数量
    printf("program header number : %d\n", elf_header.e_phnum);
    // 遍历程序头
    traveralProgramHeader(fd, &elf_header, &prog_header);

    // section 数量
    printf("section header number : %d\n", elf_header.e_shnum);
    // 遍历 section table 并 解析 字符串 表
    memset(string_table, 0, sizeof(void *) * TABLE_ENTRY_COUNT);
    traveralSectionHeader(fd, &elf_header, &section_header, string_table, &section_table_name_ref);

    // 显示每节的名称
    displaySectionNames(fd, &elf_header, &section_header, string_table, section_table_name_ref);

    // 释放 string_table 分配的内存
    for (size_t i = 0; i < TABLE_ENTRY_COUNT; i++)
    {
        if (string_table[i])
        {
            free(string_table[i]);
        }
        
    }
    close(fd);
    return 0;
}

void resolveStrTable(int fd, Elf64_Shdr * header, char ** string_table, int index){
    if(index >= 1024) return;
    char * buf = malloc(header->sh_size);
    lseek(fd, header->sh_offset, SEEK_SET);
    read(fd, buf, header->sh_size);
    string_table[index] = buf;

    // 显示 string_table 表的内容, 字符串表已 '\0' 开头， 已 '\0' 结束，所以index指向0的字符串是空字符串
    // index = 0;
    // int offset = 1;
    // while (offset < header->sh_size)
    // {
    //     /* code */
    //     printf("index : %d, name : %s\n", index, buf + offset);
    //     index++;
    //     offset += strlen(buf + offset) + 1;
    // }    
}

void resolveMagicNo(Elf64_Ehdr *elf_header){

    if (strcmp("\177ELF\2\1\1", elf_header->e_ident) == 0)
    {
        printf("this is a ELF file\n");
    }else
    {
        printf("this is not a ELF format file\n");
        exit(1);
    }  
}

void resolveFileType(Elf64_Ehdr *elf_header){
    
    switch (elf_header->e_type)
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

}

void resolveArchitecture(Elf64_Ehdr *elf_header){

    switch (elf_header->e_machine)
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
}

void traveralProgramHeader(int fd, Elf64_Ehdr *elf_header, Elf64_Phdr * prog_header){

    // 程序头偏移
    int count = 0;
    Elf64_Off hdr_offset = elf_header->e_phoff;
    while (count <= elf_header->e_phnum)
    {
        /* code */
        lseek(fd, hdr_offset, SEEK_SET);
        read(fd, prog_header, elf_header->e_phentsize);
        // 解析 program header type
        resolvePromgramHeaderType(prog_header, count);
        printf("v_addr = 0x%x, p_addr = 0x%x, file offset = 0x%x, alignment, file & memory = 0x%x\n", 
            prog_header->p_vaddr, prog_header->p_paddr, prog_header->p_offset, prog_header->p_align);
        count++;
        hdr_offset += elf_header->e_phentsize;
    }
}

void resolvePromgramHeaderType(Elf64_Phdr * prog_header, int count){

    switch (prog_header->p_type)
    {
    case PT_LOAD:
        printf("[%d] PT_LOAD\t");
        break;
    case PT_DYNAMIC:
        printf("[%d] PT_DYNAMIC\t", count);
        break;
    case PT_INTERP:
        printf("[%d] PT_INTERP\t", count);
        break;
    case PT_SHLIB:
        printf("[%d] PT_SHLIB\t", count);
        break;
    case PT_NOTE:
        printf("[%d] PT_NOTE\t", count);
        break;
    case PT_PHDR:
        printf("[%d] PT_PHDR\t", count);
        break;
    case PT_NULL:
        printf("[%d] PT_NULL\t", count);
        break;
    default:
        printf("[%d] unkown\t", count);
        break;
    }
}

void traveralSectionHeader(int fd, Elf64_Ehdr * elf_header, Elf64_Shdr * section_header, char ** string_table, int * section_table_name_ref){

    // 节头偏移
    int count = 0;
    Elf64_Off hdr_offset = elf_header->e_shoff;

    while (count <= elf_header->e_shnum)
    {
        /* code */
        lseek(fd, hdr_offset, SEEK_SET);
        read(fd, section_header, elf_header->e_shentsize);

        if (count == elf_header->e_shstrndx)
        {   // section table 名字对应的 字符串 表索引
            *section_table_name_ref = count;
        }
        
        switch (section_header->sh_type)
        {
        case SHT_STRTAB:
            printf("SHT_STRTAB section\n");
            resolveStrTable(fd, section_header, string_table, count);
            break;
        default:
            break;
        }
        printf("[%d] sh_addr = 0x%x, sh_offset = 0x%x, sh_size = 0x%x, name index = %d\n",
             count, section_header->sh_addr, section_header->sh_offset, section_header->sh_size, section_header->sh_name);

        count++;
        hdr_offset += elf_header->e_shentsize;  
    }
}

void displaySectionNames(int fd, Elf64_Ehdr * elf_header, Elf64_Shdr * section_header, char ** string_table, int section_table_name_ref){

    // 节头偏移
    int count = 0;
    Elf64_Off hdr_offset = elf_header->e_shoff;
    while (count <= elf_header->e_shnum){
        lseek(fd, hdr_offset, SEEK_SET);
        read(fd, section_header, elf_header->e_shentsize);
        printf("[%d] section name index : %d\t", count, section_header->sh_name);
        printf("section name : %s\n", string_table[section_table_name_ref] + section_header->sh_name);
        count++;
        hdr_offset += elf_header->e_shentsize;
    }
}