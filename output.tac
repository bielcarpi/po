global:
	b = ^1001
	a = 3

main:
	b = ^1002
	savec
	addp 0 ^1003
	call prints
	loadc
	ret 0

PRINT_STR:
	syscall 4
	ret 0


