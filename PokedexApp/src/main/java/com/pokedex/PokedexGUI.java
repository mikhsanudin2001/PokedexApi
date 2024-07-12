package com.pokedex;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class PokedexGUI {
    private JFrame frame;
    private JPanel pokemonPanel;
    private JPanel detailPanel;
    private JTextArea pokemonDetails;
    private JLabel pokemonImageLabel;
    private JButton backButton;

    public PokedexGUI() {
        frame = new JFrame("Pokedex");
        pokemonPanel = new JPanel();
        detailPanel = new JPanel();
        pokemonDetails = new JTextArea();
        pokemonDetails.setEditable(false);
        pokemonImageLabel = new JLabel();
        backButton = new JButton("Back");
    }

    public void createAndShowGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Panel detail
        detailPanel.setLayout(new GridBagLayout()); // Menggunakan GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margin

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Pusatkan gambar
        detailPanel.add(pokemonImageLabel, gbc); // Gambar di tengah

        gbc.gridy = 1;
        detailPanel.add(pokemonDetails, gbc); // Detail di bawah gambar

        gbc.gridy = 2;
        detailPanel.add(backButton, gbc); // Tombol di bawah detail

        detailPanel.setVisible(false); // Tersembunyi awalnya
        frame.getContentPane().add(detailPanel, BorderLayout.NORTH);

        // Fetch the list of Pokemon
        List<Pokemon> pokemon = PokemonFetcher.fetchPokemon();
        pokemonPanel.setLayout(new GridLayout(0, 5)); // 5 kolom, otomatis baris

        for (Pokemon p : pokemon) {
            try {
                URL url = new URI(p.getImageUrl()).toURL();
                ImageIcon imageIcon = new ImageIcon(url);
                Image img = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                JLabel label = new JLabel(new ImageIcon(img));

                // Menambahkan MouseListener
                label.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        displayPokemonDetails(p);
                    }
                });

                pokemonPanel.add(label);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        JScrollPane scrollPane = new JScrollPane(pokemonPanel);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);

        // Mengatur aksi untuk tombol Back
        backButton.addActionListener(e -> showPokemonPanel());
    }

    private void displayPokemonDetails(Pokemon pokemon) {
        StringBuilder details = new StringBuilder();
        details.append("Name: ").append(pokemon.getName()).append("\n");
        details.append("Height: ").append(pokemon.getHeight()).append("\n");
        details.append("Weight: ").append(pokemon.getWeight()).append("\n");
        details.append("Types: ");
        for (String type : pokemon.getTypes()) {
            details.append(type).append(" ");
        }
        details.append("\n");

        pokemonDetails.setText(details.toString());

        // Memuat gambar ke panel detail
        try {
            URL url = new URI(pokemon.getImageUrl()).toURL();
            ImageIcon imageIcon = new ImageIcon(url);
            Image img = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            pokemonImageLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            e.printStackTrace();
            pokemonImageLabel.setIcon(null);
        }

        // Menampilkan panel detail
        pokemonPanel.setVisible(false); // Sembunyikan panel gambar
        detailPanel.setVisible(true); // Tampilkan panel detail
    }

    private void showPokemonPanel() {
        detailPanel.setVisible(false); // Sembunyikan panel detail
        pokemonPanel.setVisible(true); // Tampilkan panel gambar
    }
}
