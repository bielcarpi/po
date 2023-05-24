PRINT_INT:
	syscall 1
	ret 0

global:
	a = 1
	b = 2
	c = 3
	d = 4

main:
	s0 = d + 1
	e = s0
	s0 = e + 1
	f = s0
	s0 = f + 1
	g = s0
	s0 = g + 1
	h = s0
	s0 = h + 1
	i = s0
	savec
	addp 0 i
	call print
	loadc
	ret 0


