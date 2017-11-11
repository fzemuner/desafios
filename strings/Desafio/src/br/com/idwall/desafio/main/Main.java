package br.com.idwall.desafio.main;

import br.com.idwall.desafio.util.FormataTextoUtil;

public class Main {

	public static void main(String[] args) {
		if (args != null && args.length >0) {
			System.out.println( FormataTextoUtil.formatarTexto(args[0]) );
		}
	}
	
}
