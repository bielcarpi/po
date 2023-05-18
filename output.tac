global:
	a = 3

main:
E0:
	if a >= 13 goto E1
	t9 = a + 1
	a = t9
	goto E0
E1:
	ret 0


