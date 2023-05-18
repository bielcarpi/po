for:
	i = 0
E0:
	if i > 10 goto E1
	a = a + 1
	i = i + 1
	goto E0
E1:
	ret 0

global:
	a = 3
	b = 2


