fibonacci:
	i = 0
E0:
	if n > 1 goto E2
E1:
	ret n
E2:
	fib = 1
	prevFib = 1
	i = 2
E3:
	if i >= n goto E4
	temp = fib
	s0 = fib + prevFib
	fib = s0
	prevFib = temp
	i = i + 1
	goto E3
E4:
	ret fib

PRINT_INT:
	syscall 1
	a0 = 0xA
	syscall 11
	ret 0

READ_INT:
	syscall 5
	ret v0

global:

main:
	savec
	addp 0 $z1101
	call prints
	loadc
	savec
	addp 0 $z1102
	call prints
	loadc
	savec
	call read
	loadc
	num = v0
	savec
	addp 0 num
	call fibonacci
	loadc
	result = v0
	savec
	addp 0 $z1103
	call prints
	loadc
	savec
	addp 0 result
	call print
	loadc
	ret 0

PRINT_STR:
	syscall 4
	ret 0


