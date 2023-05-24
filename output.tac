PRINT_INT:
	syscall 1
	ret 0

global:
	n = 10
	firstTerm = 0

main:
	call hola
	ret 0

hola:
	secondTerm = 1
	addp 0 "hola"
	call print
	i = 0
E0:
	if i >= n goto E1
	s0 = firstTerm + secondTerm
	nextTerm = s0
	firstTerm = secondTerm
	secondTerm = nextTerm
	i = i + 1
	goto E0
E1:
	ret 0


