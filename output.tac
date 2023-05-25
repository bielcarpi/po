PRINT_INT:
	syscall 1
	a0 = 0xA
	syscall 11
	ret 0

main:
	savec
	addp 0 b
	call print
	loadc
	ret 0


