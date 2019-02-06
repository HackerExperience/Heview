dev-css-bsd:
	touch css.trigger
	$(MAKE) dev-css-compile;
	- while :; do $(MAKE) dev-css-compile; sleep 1; done

dev-css-compile:
	@./compile-css.sh
