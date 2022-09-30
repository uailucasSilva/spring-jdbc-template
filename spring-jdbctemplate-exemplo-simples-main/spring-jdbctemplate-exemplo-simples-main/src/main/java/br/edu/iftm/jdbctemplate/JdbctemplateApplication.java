package br.edu.iftm.jdbctemplate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class JdbctemplateApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(JdbctemplateApplication.class, args);
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public void run(String... args) throws Exception {

		jdbcTemplate.execute("DROP TABLE contatos IF EXISTS");
		jdbcTemplate.execute("CREATE TABLE contatos(id SERIAL, nome VARCHAR(255), telefone VARCHAR(255) )");

		jdbcTemplate.update("INSERT INTO contatos(nome,telefone) VALUES (?,?)", "Edson Angoti Júnior", "123456");
		jdbcTemplate.update("INSERT INTO contatos(nome,telefone) VALUES (?,?)", "José Joaquim", "123");
		jdbcTemplate.update("INSERT INTO contatos(nome,telefone) VALUES (?,?)", "Maria Carolina", "123");
		jdbcTemplate.update("INSERT INTO contatos(nome,telefone) VALUES (?,?)", "Juca Silva", "44444");

		List<Contato> contatos = jdbcTemplate.query("SELECT id, nome, telefone FROM contatos", (rs, rowNum) -> {
			return new Contato(rs.getLong("id"), rs.getString("nome"), rs.getString("telefone"));
		});
		System.out.println("Listando contatos");
		// Contato contato var -> de indice : contatos -> a lista que vai ser
		// percorrida;
		for (Contato contato : contatos) {
			System.out.println(contato.getNome() + " - " + contato.getTelefone());
		}

		// retorna a quantidade de linhas que tem na tabela
		int rowCount = jdbcTemplate.queryForObject("select count(*) from contatos", Integer.class);
		System.out.println("Existem " + rowCount + " registros na tabela contatos");

		// seleciona um campo da tabela utilizando uma regra para o nome
		String nroTelefone = jdbcTemplate.queryForObject(
				"select telefone from contatos where nome = ?",
				String.class, "Edson Angoti Júnior");

		System.out.println("Esse é o número do contato Edson Angoti: " + nroTelefone);
	}

}
