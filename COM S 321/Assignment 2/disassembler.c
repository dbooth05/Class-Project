// Gavin Fisher gdf73278
// Dylan Booth dbooth05
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdint.h>
#include <sys/mman.h>
#include <endian.h>
#include <search.h>


typedef struct instruction {
    const char* name;
    void (*instruction_func_t)();
    uint16_t opcode;
} instruction_t;
//R and D are 11, I is 10, B is 6, CB is 8
void decode_R_type (instruction_t, int32_t, int);
void decode_I_type (instruction_t, int32_t, int);
void decode_D_type (instruction_t, int32_t, int);
void decode_B_type (instruction_t, int32_t, int);
void decode_CB_type (instruction_t, int32_t, int);

instruction_t instruction[] = {
        { "ADD",     decode_R_type,      0b10001011000 },
        { "AND",     decode_R_type,      0b10001010000 },
        { "BR",      decode_R_type,      0b11010110000 },
        { "DUMP",    decode_R_type,      0b11111111110 },
        { "EOR",     decode_R_type,      0b11001010000 },
        { "HALT",    decode_R_type,      0b11111111111 },
        { "LSL",     decode_R_type,      0b11010011011 },
        { "LSR",     decode_R_type,      0b11010011010 },
        { "ORR",     decode_R_type,      0b10101010000 },
        { "MUL",     decode_R_type,      0b10011011000},
        { "PRNL",    decode_R_type,     0b11111111100 },
        { "PRNT",    decode_R_type,     0b11111111101 },
        { "SUB",     decode_R_type,     0b11001011000 },
        { "SUBS",    decode_R_type,     0b11101011000 },

        { "ADDI",    decode_I_type,     0b1001000100  },
        { "ANDI",    decode_I_type,     0b1001001000  },
        { "EORI",    decode_I_type,     0b1101001000  },
        { "ORRI",    decode_I_type,     0b1011001000  },
        { "SUBI",    decode_I_type,     0b1101000100  },
        { "SUBIS",   decode_I_type,     0b1111000100  },

        { "LDUR",    decode_D_type,     0b11111000010 },
        { "STUR",    decode_D_type,     0b11111000000 },

        { "B",       decode_B_type,     0b000101      },
        { "BL",      decode_B_type,     0b100101      },

        { "CBNZ",    decode_CB_type,    0b10110101    },
        { "CBZ",     decode_CB_type,    0b10110100    },
        { "B.",      decode_CB_type,    0b01010100}
};

//opcode 11, Rm 5, Shamt 6, Rn 5, Rd 5
void decode_R_type (instruction_t type, int32_t binary, int line) {
    int rm = (binary & 0x001F0000) >> 16;
    int shamt = (binary & 0x000FC00) >> 10;
    int rn = (binary & 0x000003E0) >> 5;
    int rd = (binary & 0x0000001F);

    if (type.name == "HALT") {
        printf("%s\n", "HALT");
    } else if (type.name == "DUMP") {
        printf("%s\n", "DUMP");
    } else if (type.name == "PRNL") {
        printf("%s\n", "PRNL");
    } else if (type.name == "PRNT") {
        printf("%s X%d\n", "PRNT", rd);
    } else if (type.name == "LSL") {
        printf("%s X%d, X%d, #%d\n", type.name, rd, rn, (signed char) shamt);
    } else if (type.name == "LSR") {
        printf("%s X%d, X%d, #%d\n", type.name, rd, rn, (signed char) shamt);
    } else if (type.name == "BR") {
        if (rn == 30) {
            printf("%s %s\n", type.name, "LR");
        }
        else {
            printf("%s X%d\n", type.name, rn);
        }
    } else {
        printf("%s X%d, X%d, X%d\n", type.name, rd, rn, rm);
    }

}

//opcode 10, ALU_immediate 12, Rn 5, Rd 5
void decode_I_type (instruction_t type, int32_t binary, int line) {
    int ALU = (binary & 0x003FFC00) >> 10;
    int rn = (binary & 0x000003E0) >> 5;
    int rd = (binary & 0x0000001F);
    printf("%s X%d, X%d, #%d\n", type.name, rd, rn, (signed char) ALU);


}


//opcode 11, DT_address 9, op 2, Rn 5, Rt 5
void decode_D_type (instruction_t type, int32_t binary, int line) {
    int dt = (binary & 0x001FF000) >> 12;
    int op = (binary & 0x00000C00) >> 10; //Haven't used in class
    int rn = (binary & 0x000003E0) >> 5;
    int rt = (binary & 0x0000001F);
    printf("%s X%d, [X%d, #%d]\n", type.name, rt, rn, dt);
}

//opcode 6, BR_address 26
void decode_B_type (instruction_t type, int32_t binary, int line) {
    int br = (binary & 0x0CFFFFFF);
    printf("%s %s%d\n", type.name, "label_", (signed char) br);
}

//opcode 8, COND_BR_address 19, Rt 5
void decode_CB_type (instruction_t type, int32_t binary, int line) {
    int cond = (binary & 0x00FFFFE0) >> 5;
    int rt = (binary & 0x0000001F);


    cond += line;

    if (type.name == "B.") {
        switch (rt) {
            case 0:
                printf("%s%s %s%d\n", type.name, "EQ", "label_", cond);
                break;
            case 1:
                printf("%s%s %s%d\n", type.name, "NE", "label_", cond);
                break;
            case 2:
                printf("%s%s %s%d\n", type.name, "HS", "label_", cond);
                break;
            case 3:
                printf("%s%s %s%d\n", type.name, "LO", "label_", cond);
                break;
            case 4:
                printf("%s%s %s%d\n", type.name, "MI", "label_", cond);
                break;
            case 5:
                printf("%s%s %s%d\n", type.name, "PL", "label_", cond);
                break;
            case 6:
                printf("%s%s %s%d\n", type.name, "VS", "label_", cond);
                break;
            case 7:
                printf("%s%s %s%d\n", type.name, "VC", "label_", cond);
                break;
            case 8:
                printf("%s%s %s%d\n", type.name, "HI", "label_", cond);
                break;
            case 9:
                printf("%s%s %s%d\n", type.name, "LS", "label_", cond);
                break;
            case 10:
                printf("%s%s %s%d\n", type.name, "GE", "label_", cond);
                break;
            case 11:
                printf("%s%s %s%d\n", type.name, "LT", "label_", cond);
                break;
            case 12:
                printf("%s%s %s%d\n", type.name, "GT", "label_", cond);
                break;
            case 13:
                printf("%s%s %s%d\n", type.name, "LE", "label_", cond);
                break;
        }
    } else {
        printf("%s X%d, %s%d\n", type.name, rt, "label_", cond);
    }

}

void decode(int program, int line){
    //basically lets say 1111100000 is what we are looking at
    //the first 5 bits return true because that's what we want for opcode
    //next 5 bits are false because that's other code
    unsigned int op11bit = (program & 0xFFE00000) >> 21;
    unsigned int op10bit = (program & 0xFFC00000) >> 22;
    unsigned int op8bit = (program & 0xFF000000) >> 24;
    unsigned int op6bit = (program & 0xFc000000) >> 26;

    for (int i = 0; i < (sizeof(instruction) / sizeof(instruction[0])); i++) {
        if (op11bit == instruction[i].opcode){
            if (instruction[i].instruction_func_t == decode_R_type) {
                decode_R_type(instruction[i], program, line);
            } else {
                decode_D_type(instruction[i], program, line);
            }
        } else if (op10bit == instruction[i].opcode) {
            decode_I_type(instruction[i], program, line);
        } else if (op8bit == instruction[i].opcode) {
            decode_CB_type(instruction[i], program, line);
        } else if (op6bit == instruction[i].opcode) {
            decode_B_type(instruction[i], program, line);
        }
    }

}

void emulate(){

}

int main(int argc, char *argv[]) {
    int fd;
    struct stat buf;
    int i;
    int m;
    int *bprogram;
    int32_t* program;


    fd = open(argv[1], O_RDONLY);
    fstat(fd, &buf);
    program = mmap(NULL, buf.st_size, PROT_READ | PROT_WRITE, MAP_PRIVATE, fd, 0);
    bprogram = calloc(buf.st_size / 4, sizeof (*bprogram));
    for (i = 0; i < (buf.st_size / 4); i++) {
        program[i] = be32toh(program[i]);
        printf("%s%d%s\n", "label_",i, ":");
        decode(program[i], i);
        printf("\n");
//        printf("%x\n", program[i]);
    }
    emulate(bprogram, buf.st_size / 4, &m);
    return 0;
}


