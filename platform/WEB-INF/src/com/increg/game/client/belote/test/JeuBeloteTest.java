/*
 * Created on 24 avr. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.increg.game.client.belote.test;

import java.util.Vector;

import com.increg.game.client.Couleur;
import com.increg.game.client.belote.AnnonceBelote;
import com.increg.game.client.belote.AtoutBelote;
import com.increg.game.client.belote.CouleurBelote;
import com.increg.game.client.belote.JeuBelote;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Manu
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JeuBeloteTest extends TestCase {


    /**
     * Teste l'enchainement des états de belote
     *
     */
    public void testDistribution() {
        JeuBelote jeu = new JeuBelote(4);
        
        jeu.melangeTas();
        
        Assert.assertEquals(jeu.getTas().size(), 32);
        
        jeu.distribue(0, 2, 1, 0);
        
        Assert.assertEquals(jeu.getMains(0).size(), 5);
        Assert.assertEquals(jeu.getMains(1).size(), 5);
        Assert.assertEquals(jeu.getMains(2).size(), 5);
        Assert.assertEquals(jeu.getMains(3).size(), 5);

        jeu.distribue(1, 2, 1, 0);
        
        Assert.assertEquals(jeu.getMains(0).size(), 8);
        Assert.assertEquals(jeu.getMains(1).size(), 8);
        Assert.assertEquals(jeu.getMains(2).size(), 8);
        Assert.assertEquals(jeu.getMains(3).size(), 8);
        Assert.assertEquals(jeu.getTas().size(), 0);
        
        jeu.triMains();

        Assert.assertEquals(jeu.getMains(0).size(), 8);
        Assert.assertEquals(jeu.getMains(1).size(), 8);
        Assert.assertEquals(jeu.getMains(2).size(), 8);
        Assert.assertEquals(jeu.getMains(3).size(), 8);
        Assert.assertEquals(jeu.getTas().size(), 0);
    }

    /**
     * Teste la recherche d'annonces
     *
     */
    public void testChercheAnnonce() {
        
        JeuBelote jeu = new JeuBelote(4);
        {
        CouleurBelote septCarreau = new CouleurBelote(CouleurBelote.SEPT, CouleurBelote.CARREAU);
        CouleurBelote huitCarreau = new CouleurBelote(CouleurBelote.HUIT, CouleurBelote.CARREAU);
        CouleurBelote neufCarreau = new CouleurBelote(CouleurBelote.NEUF, CouleurBelote.CARREAU);
        CouleurBelote dixCarreau = new CouleurBelote(CouleurBelote.DIX, CouleurBelote.CARREAU);
        CouleurBelote valetCarreau = new CouleurBelote(CouleurBelote.VALET, CouleurBelote.CARREAU);
        CouleurBelote dameCarreau = new CouleurBelote(CouleurBelote.DAME, CouleurBelote.CARREAU);
        CouleurBelote roiCarreau = new CouleurBelote(CouleurBelote.ROI, CouleurBelote.CARREAU);
        CouleurBelote asCarreau = new CouleurBelote(CouleurBelote.AS, CouleurBelote.CARREAU);
        CouleurBelote septTrefle = new CouleurBelote(CouleurBelote.SEPT, CouleurBelote.TREFLE);
        CouleurBelote huitTrefle = new CouleurBelote(CouleurBelote.HUIT, CouleurBelote.TREFLE);
        CouleurBelote neufTrefle = new CouleurBelote(CouleurBelote.NEUF, CouleurBelote.TREFLE);
        CouleurBelote dixTrefle = new CouleurBelote(CouleurBelote.DIX, CouleurBelote.TREFLE);
        CouleurBelote valetTrefle = new CouleurBelote(CouleurBelote.VALET, CouleurBelote.TREFLE);
        CouleurBelote dameTrefle = new CouleurBelote(CouleurBelote.DAME, CouleurBelote.TREFLE);
        CouleurBelote roiTrefle = new CouleurBelote(CouleurBelote.ROI, CouleurBelote.TREFLE);
        CouleurBelote asTrefle = new CouleurBelote(CouleurBelote.AS, CouleurBelote.TREFLE);
        CouleurBelote septCoeur = new CouleurBelote(CouleurBelote.SEPT, CouleurBelote.COEUR);
        CouleurBelote huitCoeur = new CouleurBelote(CouleurBelote.HUIT, CouleurBelote.COEUR);
        CouleurBelote neufCoeur = new CouleurBelote(CouleurBelote.NEUF, CouleurBelote.COEUR);
        CouleurBelote dixCoeur = new CouleurBelote(CouleurBelote.DIX, CouleurBelote.COEUR);
        CouleurBelote valetCoeur = new CouleurBelote(CouleurBelote.VALET, CouleurBelote.COEUR);
        CouleurBelote dameCoeur = new CouleurBelote(CouleurBelote.DAME, CouleurBelote.COEUR);
        CouleurBelote roiCoeur = new CouleurBelote(CouleurBelote.ROI, CouleurBelote.COEUR);
        CouleurBelote asCoeur = new CouleurBelote(CouleurBelote.AS, CouleurBelote.COEUR);
        AtoutBelote septPique = new AtoutBelote(AtoutBelote.SEPT, CouleurBelote.PIQUE);
        AtoutBelote huitPique = new AtoutBelote(AtoutBelote.HUIT, CouleurBelote.PIQUE);
        AtoutBelote neufPique = new AtoutBelote(AtoutBelote.NEUF, CouleurBelote.PIQUE);
        AtoutBelote dixPique = new AtoutBelote(AtoutBelote.DIX, CouleurBelote.PIQUE);
        AtoutBelote valetPique = new AtoutBelote(AtoutBelote.VALET, CouleurBelote.PIQUE);
        AtoutBelote damePique = new AtoutBelote(AtoutBelote.DAME, CouleurBelote.PIQUE);
        AtoutBelote roiPique = new AtoutBelote(AtoutBelote.ROI, CouleurBelote.PIQUE);
        AtoutBelote asPique = new AtoutBelote(AtoutBelote.AS, CouleurBelote.PIQUE);

        /**
         * 1er test : Rien à trouver
         */
        Vector[] mains = new Vector[4];
        mains[0] = new Vector();
        mains[0].add(septCarreau);        
        mains[0].add(neufCarreau);        
        mains[0].add(valetCarreau);        
        mains[0].add(roiCarreau);        
        mains[0].add(septCoeur);        
        mains[0].add(neufCoeur);        
        mains[0].add(valetCoeur);        
        mains[0].add(roiCoeur);        
        mains[1] = new Vector();
        mains[1].add(septPique);        
        mains[1].add(neufPique);        
        mains[1].add(valetPique);        
        mains[1].add(roiPique);        
        mains[1].add(septTrefle);        
        mains[1].add(neufTrefle);        
        mains[1].add(valetTrefle);        
        mains[1].add(roiTrefle);        
        mains[2] = new Vector();
        mains[2].add(huitCarreau);        
        mains[2].add(dameCarreau);        
        mains[2].add(dixCarreau);        
        mains[2].add(asCarreau);        
        mains[2].add(huitCoeur);        
        mains[2].add(dameCoeur);        
        mains[2].add(dixCoeur);        
        mains[2].add(asCoeur);        
        mains[3] = new Vector();
        mains[3].add(huitPique);        
        mains[3].add(damePique);        
        mains[3].add(dixPique);        
        mains[3].add(asPique);        
        mains[3].add(huitTrefle);        
        mains[3].add(dameTrefle);        
        mains[3].add(dixTrefle);        
        mains[3].add(asTrefle);        

        jeu.setMains(mains);
        Vector[] annonces = jeu.chercheAnnonce();
        Assert.assertEquals(annonces[0].size(), 0);
        Assert.assertEquals(annonces[1].size(), 0);
        Assert.assertEquals(annonces[2].size(), 0);
        Assert.assertEquals(annonces[3].size(), 0);

        /**
         * 2ème test : Carrés à trouver dont "faux" carrés
         */
        mains = new Vector[4];
        mains[0] = new Vector();
        mains[0].add(septCarreau);        
        mains[0].add(neufCarreau);        
        mains[0].add(valetCarreau);        
        mains[0].add(septTrefle);        
        mains[0].add(neufTrefle);        
        mains[0].add(septCoeur);        
        mains[0].add(neufCoeur);        
        mains[0].add(septPique);        
        mains[1] = new Vector();
        mains[1].add(valetCoeur);        
        mains[1].add(roiCoeur);        
        mains[1].add(roiPique);        
        mains[1].add(neufPique);        
        mains[1].add(valetPique);        
        mains[1].add(roiCarreau);        
        mains[1].add(valetTrefle);        
        mains[1].add(roiTrefle);        
        mains[2] = new Vector();
        mains[2].add(huitCarreau);        
        mains[2].add(dameCarreau);        
        mains[2].add(dixCarreau);        
        mains[2].add(asCarreau);        
        mains[2].add(huitCoeur);        
        mains[2].add(dameCoeur);        
        mains[2].add(dixCoeur);        
        mains[2].add(asCoeur);        
        mains[3] = new Vector();
        mains[3].add(huitPique);        
        mains[3].add(damePique);        
        mains[3].add(dixPique);        
        mains[3].add(asPique);        
        mains[3].add(huitTrefle);        
        mains[3].add(dameTrefle);        
        mains[3].add(dixTrefle);        
        mains[3].add(asTrefle);        

        jeu.setMains(mains);
        annonces = jeu.chercheAnnonce();
        Assert.assertEquals(annonces[0].size(), 0); // Carré de 7 filtré
        Assert.assertEquals(annonces[1].size(), 1); // Carré de rois
        Assert.assertEquals(annonces[2].size(), 0);
        Assert.assertEquals(annonces[3].size(), 0);

        /**
         * 3ème test : Carrés à trouver dont "faux" carrés
         * Tierce, 50
         */
        mains = new Vector[4];
        mains[0] = new Vector(); // Carré de 7
        mains[0].add(septCarreau);        
        mains[0].add(valetCarreau);        
        mains[0].add(dameCarreau);        
        mains[0].add(septTrefle);        
        mains[0].add(neufTrefle);        
        mains[0].add(septCoeur);        
        mains[0].add(neufCoeur);        
        mains[0].add(septPique);        
        mains[1] = new Vector(); // Carré de rois
        mains[1].add(valetCoeur);        
        mains[1].add(roiCoeur);        
        mains[1].add(damePique);        
        mains[1].add(roiPique);        
        mains[1].add(roiCarreau);        
        mains[1].add(huitTrefle);        
        mains[1].add(valetTrefle);        
        mains[1].add(roiTrefle);        
        mains[2] = new Vector(); // Tierce
        mains[2].add(huitCarreau);        
        mains[2].add(neufCarreau);        
        mains[2].add(dixCarreau);        
        mains[2].add(asCarreau);        
        mains[2].add(huitCoeur);        
        mains[2].add(dameCoeur);        
        mains[2].add(dixCoeur);        
        mains[2].add(asCoeur);        
        mains[3] = new Vector(); // 50
        mains[3].add(huitPique);        
        mains[3].add(dixPique);        
        mains[3].add(asPique);        
        mains[3].add(neufPique);        
        mains[3].add(valetPique);        
        mains[3].add(dameTrefle);        
        mains[3].add(dixTrefle);        
        mains[3].add(asTrefle);        

        jeu.setMains(mains);
        annonces = jeu.chercheAnnonce();
        Assert.assertEquals(annonces[0].size(), 0); // Carré de 7 filtré
        Assert.assertEquals(annonces[1].size(), 1); // Carré de rois
        Assert.assertEquals(((AnnonceBelote) annonces[1].get(0)).getType(), AnnonceBelote.TYPE_CARRE); // Carré de rois
        Assert.assertEquals(annonces[2].size(), 1); // Tierce à carreau
        Assert.assertEquals(((AnnonceBelote) annonces[2].get(0)).getType(), AnnonceBelote.TYPE_TIERCE); // Tierce
        Assert.assertEquals(annonces[3].size(), 1);
        Assert.assertEquals(((AnnonceBelote) annonces[3].get(0)).getType(), AnnonceBelote.TYPE_QUATRIEME); // 50


        /**
         * 4ème test : Tierce à l'atout, 50, 100 et 6 cartes 
         */
        mains = new Vector[4];
        mains[0] = new Vector(); 
        mains[0].add(septCarreau);        
        mains[0].add(asCarreau);        
        mains[0].add(dameCarreau);        
        mains[0].add(septTrefle);        
        mains[0].add(neufTrefle);        
        mains[0].add(septCoeur);        
        mains[0].add(neufCoeur);        
        mains[0].add(septPique);        
        mains[1] = new Vector(); // Tierce
        mains[1].add(valetCoeur);        
        mains[1].add(roiCoeur);        
        mains[1].add(huitPique);        
        mains[1].add(roiCarreau);        
        mains[1].add(dameTrefle);        
        mains[1].add(huitTrefle);        
        mains[1].add(valetTrefle);        
        mains[1].add(roiTrefle);        
        mains[2] = new Vector(); // 50
        mains[2].add(huitCarreau);        
        mains[2].add(neufCarreau);        
        mains[2].add(dixCarreau);        
        mains[2].add(valetCarreau);        
        mains[2].add(huitCoeur);        
        mains[2].add(dameCoeur);        
        mains[2].add(dixCoeur);        
        mains[2].add(asCoeur);        
        mains[3] = new Vector(); // 100 à l'atout avec 6 cartes
        mains[3].add(damePique);        
        mains[3].add(dixPique);        
        mains[3].add(asPique);        
        mains[3].add(neufPique);        
        mains[3].add(valetPique);        
        mains[3].add(roiPique);        
        mains[3].add(dixTrefle);        
        mains[3].add(asTrefle);        

        jeu.setMains(mains);
        annonces = jeu.chercheAnnonce();
        Assert.assertEquals(annonces[0].size(), 0); // Carré de 7 filtré
        Assert.assertEquals(annonces[1].size(), 1); // Tierce
        Assert.assertEquals(((AnnonceBelote) annonces[1].get(0)).getType(), AnnonceBelote.TYPE_TIERCE); // Tierce
        Assert.assertEquals(annonces[2].size(), 1); // 50
        Assert.assertEquals(((AnnonceBelote) annonces[2].get(0)).getType(), AnnonceBelote.TYPE_QUATRIEME); // 50
        Assert.assertEquals(annonces[3].size(), 1);
        Assert.assertEquals(((AnnonceBelote) annonces[3].get(0)).getType(), AnnonceBelote.TYPE_CENT); // 100
        }
        /**
         * 5ème test : Cas du bug sur indice dans ChercheAnnonce 
         */
        {
        CouleurBelote septCarreau = new CouleurBelote(CouleurBelote.SEPT, CouleurBelote.CARREAU);
        CouleurBelote huitCarreau = new CouleurBelote(CouleurBelote.HUIT, CouleurBelote.CARREAU);
        CouleurBelote neufCarreau = new CouleurBelote(CouleurBelote.NEUF, CouleurBelote.CARREAU);
        CouleurBelote dixCarreau = new CouleurBelote(CouleurBelote.DIX, CouleurBelote.CARREAU);
        CouleurBelote valetCarreau = new CouleurBelote(CouleurBelote.VALET, CouleurBelote.CARREAU);
        CouleurBelote dameCarreau = new CouleurBelote(CouleurBelote.DAME, CouleurBelote.CARREAU);
        CouleurBelote roiCarreau = new CouleurBelote(CouleurBelote.ROI, CouleurBelote.CARREAU);
        CouleurBelote asCarreau = new CouleurBelote(CouleurBelote.AS, CouleurBelote.CARREAU);
        CouleurBelote septPique = new CouleurBelote(CouleurBelote.SEPT, CouleurBelote.PIQUE);
        CouleurBelote huitPique = new CouleurBelote(CouleurBelote.HUIT, CouleurBelote.PIQUE);
        CouleurBelote neufPique = new CouleurBelote(CouleurBelote.NEUF, CouleurBelote.PIQUE);
        CouleurBelote dixPique = new CouleurBelote(CouleurBelote.DIX, CouleurBelote.PIQUE);
        CouleurBelote valetPique = new CouleurBelote(CouleurBelote.VALET, CouleurBelote.PIQUE);
        CouleurBelote damePique = new CouleurBelote(CouleurBelote.DAME, CouleurBelote.PIQUE);
        CouleurBelote roiPique = new CouleurBelote(CouleurBelote.ROI, CouleurBelote.PIQUE);
        CouleurBelote asPique = new CouleurBelote(CouleurBelote.AS, CouleurBelote.PIQUE);
        CouleurBelote septCoeur = new CouleurBelote(CouleurBelote.SEPT, CouleurBelote.COEUR);
        CouleurBelote huitCoeur = new CouleurBelote(CouleurBelote.HUIT, CouleurBelote.COEUR);
        CouleurBelote neufCoeur = new CouleurBelote(CouleurBelote.NEUF, CouleurBelote.COEUR);
        CouleurBelote dixCoeur = new CouleurBelote(CouleurBelote.DIX, CouleurBelote.COEUR);
        CouleurBelote valetCoeur = new CouleurBelote(CouleurBelote.VALET, CouleurBelote.COEUR);
        CouleurBelote dameCoeur = new CouleurBelote(CouleurBelote.DAME, CouleurBelote.COEUR);
        CouleurBelote roiCoeur = new CouleurBelote(CouleurBelote.ROI, CouleurBelote.COEUR);
        CouleurBelote asCoeur = new CouleurBelote(CouleurBelote.AS, CouleurBelote.COEUR);
        AtoutBelote septTrefle = new AtoutBelote(AtoutBelote.SEPT, CouleurBelote.TREFLE);
        AtoutBelote huitTrefle = new AtoutBelote(AtoutBelote.HUIT, CouleurBelote.TREFLE);
        AtoutBelote neufTrefle = new AtoutBelote(AtoutBelote.NEUF, CouleurBelote.TREFLE);
        AtoutBelote dixTrefle = new AtoutBelote(AtoutBelote.DIX, CouleurBelote.TREFLE);
        AtoutBelote valetTrefle = new AtoutBelote(AtoutBelote.VALET, CouleurBelote.TREFLE);
        AtoutBelote dameTrefle = new AtoutBelote(AtoutBelote.DAME, CouleurBelote.TREFLE);
        AtoutBelote roiTrefle = new AtoutBelote(AtoutBelote.ROI, CouleurBelote.TREFLE);
        AtoutBelote asTrefle = new AtoutBelote(AtoutBelote.AS, CouleurBelote.TREFLE);

        Vector[] mains = new Vector[4];
        mains[0] = new Vector(); 
        mains[0].add(dameCarreau);        
        mains[0].add(huitTrefle);        
        mains[0].add(valetTrefle);        
        mains[0].add(neufTrefle);        
        mains[0].add(dixTrefle);        
        mains[0].add(dameCoeur);        
        mains[0].add(dameTrefle);        
        mains[0].add(roiTrefle);        
        mains[1] = new Vector(); 
        mains[1].add(asCarreau);        
        mains[1].add(neufCarreau);        
        mains[1].add(asTrefle);        
        mains[1].add(septPique);        
        mains[1].add(neufCoeur);        
        mains[1].add(neufPique);        
        mains[1].add(damePique);        
        mains[1].add(dixPique);        
        mains[2] = new Vector(); 
        mains[2].add(septCarreau);        
        mains[2].add(roiCarreau);        
        mains[2].add(dixCoeur);        
        mains[2].add(valetCoeur);        
        mains[2].add(valetCarreau);        
        mains[2].add(septCoeur);        
        mains[2].add(huitPique);        
        mains[2].add(huitCarreau);        
        mains[3] = new Vector(); 
        mains[3].add(dixCarreau);        
        mains[3].add(septTrefle);        
        mains[3].add(huitCoeur);        
        mains[3].add(valetPique);        
        mains[3].add(roiPique);        
        mains[3].add(asCoeur);        
        mains[3].add(asPique);        
        mains[3].add(roiCoeur);        

        jeu.setMains(mains);
        
        jeu.triMains();
        
        
        Vector[] annonces = jeu.chercheAnnonce();
        Assert.assertEquals(annonces[0].size(), 1); // 100
        Assert.assertEquals(((AnnonceBelote) annonces[0].get(0)).getType(), AnnonceBelote.TYPE_CENT); // 100
        Assert.assertEquals(annonces[1].size(), 0); // Rien
        Assert.assertEquals(annonces[2].size(), 0); // Rien
        Assert.assertEquals(annonces[3].size(), 0);
        }
    }

    /**
     * Teste si le tri d'une main est stable
     * Utilité du test ?
     */
    public void testStabiliteTri() {
        
        CouleurBelote septCarreau = new CouleurBelote(CouleurBelote.SEPT, CouleurBelote.CARREAU);
        CouleurBelote huitCarreau = new CouleurBelote(CouleurBelote.HUIT, CouleurBelote.CARREAU);
        CouleurBelote neufCarreau = new CouleurBelote(CouleurBelote.NEUF, CouleurBelote.CARREAU);
        CouleurBelote dixCarreau = new CouleurBelote(CouleurBelote.DIX, CouleurBelote.CARREAU);
        CouleurBelote valetCarreau = new CouleurBelote(CouleurBelote.VALET, CouleurBelote.CARREAU);
        CouleurBelote dameCarreau = new CouleurBelote(CouleurBelote.DAME, CouleurBelote.CARREAU);
        CouleurBelote roiCarreau = new CouleurBelote(CouleurBelote.ROI, CouleurBelote.CARREAU);
        CouleurBelote asCarreau = new CouleurBelote(CouleurBelote.AS, CouleurBelote.CARREAU);
        CouleurBelote septPique = new CouleurBelote(CouleurBelote.SEPT, CouleurBelote.PIQUE);
        CouleurBelote huitPique = new CouleurBelote(CouleurBelote.HUIT, CouleurBelote.PIQUE);
        CouleurBelote neufPique = new CouleurBelote(CouleurBelote.NEUF, CouleurBelote.PIQUE);
        CouleurBelote dixPique = new CouleurBelote(CouleurBelote.DIX, CouleurBelote.PIQUE);
        CouleurBelote valetPique = new CouleurBelote(CouleurBelote.VALET, CouleurBelote.PIQUE);
        CouleurBelote damePique = new CouleurBelote(CouleurBelote.DAME, CouleurBelote.PIQUE);
        CouleurBelote roiPique = new CouleurBelote(CouleurBelote.ROI, CouleurBelote.PIQUE);
        CouleurBelote asPique = new CouleurBelote(CouleurBelote.AS, CouleurBelote.PIQUE);
        CouleurBelote septCoeur = new CouleurBelote(CouleurBelote.SEPT, CouleurBelote.COEUR);
        CouleurBelote huitCoeur = new CouleurBelote(CouleurBelote.HUIT, CouleurBelote.COEUR);
        CouleurBelote neufCoeur = new CouleurBelote(CouleurBelote.NEUF, CouleurBelote.COEUR);
        CouleurBelote dixCoeur = new CouleurBelote(CouleurBelote.DIX, CouleurBelote.COEUR);
        CouleurBelote valetCoeur = new CouleurBelote(CouleurBelote.VALET, CouleurBelote.COEUR);
        CouleurBelote dameCoeur = new CouleurBelote(CouleurBelote.DAME, CouleurBelote.COEUR);
        CouleurBelote roiCoeur = new CouleurBelote(CouleurBelote.ROI, CouleurBelote.COEUR);
        CouleurBelote asCoeur = new CouleurBelote(CouleurBelote.AS, CouleurBelote.COEUR);
        AtoutBelote septTrefle = new AtoutBelote(AtoutBelote.SEPT, CouleurBelote.TREFLE);
        AtoutBelote huitTrefle = new AtoutBelote(AtoutBelote.HUIT, CouleurBelote.TREFLE);
        AtoutBelote neufTrefle = new AtoutBelote(AtoutBelote.NEUF, CouleurBelote.TREFLE);
        AtoutBelote dixTrefle = new AtoutBelote(AtoutBelote.DIX, CouleurBelote.TREFLE);
        AtoutBelote valetTrefle = new AtoutBelote(AtoutBelote.VALET, CouleurBelote.TREFLE);
        AtoutBelote dameTrefle = new AtoutBelote(AtoutBelote.DAME, CouleurBelote.TREFLE);
        AtoutBelote roiTrefle = new AtoutBelote(AtoutBelote.ROI, CouleurBelote.TREFLE);
        AtoutBelote asTrefle = new AtoutBelote(AtoutBelote.AS, CouleurBelote.TREFLE);

        Vector[] mains = new Vector[4];
        mains[0] = new Vector(); 
        mains[0].add(dameCarreau);        
        mains[0].add(huitTrefle);        
        mains[0].add(valetTrefle);        
        mains[0].add(neufTrefle);        
        mains[0].add(dixTrefle);        
        mains[0].add(dameCoeur);        
        mains[0].add(dameTrefle);        
        mains[0].add(roiTrefle);        
        mains[1] = new Vector(); 
        mains[1].add(asCarreau);        
        mains[1].add(huitPique);        
        mains[1].add(asTrefle);        
        mains[1].add(septPique);        
        mains[1].add(neufCoeur);        
        mains[1].add(neufPique);        
        mains[1].add(damePique);        
        mains[1].add(dixPique);        
        mains[1].add(neufCarreau);        
        mains[2] = new Vector(); 
        mains[2].add(septCarreau);        
        mains[2].add(roiCarreau);        
        mains[2].add(dixCoeur);        
        mains[2].add(valetCoeur);        
        mains[2].add(valetCarreau);        
        mains[2].add(septCoeur);        
        mains[2].add(huitCarreau);        
        mains[2].add(neufCarreau);        
        mains[3] = new Vector(); 
        mains[3].add(dixCarreau);        
        mains[3].add(septTrefle);        
        mains[3].add(huitCoeur);        
        mains[3].add(valetPique);        
        mains[3].add(roiPique);        
        mains[3].add(asCoeur);        
        mains[3].add(asPique);        
        mains[3].add(roiCoeur);        

        JeuBelote jeu = new JeuBelote(4);
        jeu.setMains(mains);
        
        jeu.triMains();
        
        // Mémorise les cartes
        Couleur premiere[] = new Couleur[4];
        for (int i = 0; i  < 4; i++) {
            premiere[i] = (Couleur) mains[i].get(0); 
        }
        // Tri à nouveau
        jeu.triMains();
        // Tri à nouveau
        jeu.triMains();
        // Tri à nouveau
        jeu.triMains();
        // Tri à nouveau
        jeu.triMains();
        
        // Compare les mains
        for (int i = 0; i  < 4; i++) {
            assertTrue(premiere[i].getHauteur() == ((Couleur) mains[i].get(0)).getHauteur()); 
            assertTrue(premiere[i].getCouleur() == ((Couleur) mains[i].get(0)).getCouleur()); 
        }
    }

}
