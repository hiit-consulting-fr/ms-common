/*
 * MIT License
 *
 * Copyright (c) 2024 Hi!T Consulting
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package fr.hiitconsulting.socle.application.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation permettant de gérer le header Cache-Control
 *
 * <br><br>
 * L'en-tête HTTP Cache-Control contient des directives (c'est-à-dire des instructions),
 * dans les requêtes et dans les réponses, pour contrôler la mise en cache dans les navigateurs
 * et caches partagées (par exemple les proxies, CDN).
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheControl {

    /**
     * La directive de réponse max-age=N indique que la réponse reste fraîche jusqu'à N secondes après la génération de la réponse.
     * <br><br>
     * Cela indique que les caches peuvent stocker cette réponse et la réutiliser pour les requêtes suivantes tant qu'elle est fraîche.
     * <br><br>
     * On notera que max-age ne correspond pas au temps écoulé depuis que la réponse a été reçue,
     * il s'agit du temps écoulé depuis que la réponse a été générée sur le serveur d'origine.
     * Ainsi, si les autres caches situés sur la route réseau empruntée par la réponse stockent la réponse
     * pendant 100 secondes (en l'indiquant avec l'en-tête de réponse Age), le cache du navigateur déduira
     * 100 secondes de la durée de fraîcheur.
     */
    long maxAge() default 30;

    /**
     * La directive de réponse no-cache indique que la réponse peut être stockée en cache, mais qu'elle doit être validée
     * avec le serveur d'origine avant chaque réutilisation, même si le cache est déconnecté du serveur d'origine.
     * <br><br>
     * Si vous souhaitez que les caches vérifient leur contenu à chaque mise à jour tout en réutilisant du contenu stocké,
     * no-cache est la directive à utiliser.
     * <br><br>
     * On notera que no-cache ne signifie pas « ne pas mettre en cache ».
     * no-cache permet aux caches de stocker une réponse, mais impose une revalidation avant toute réutilisation.
     * Si vous souhaitez effectivement ne pas stocker de données pour ne pas avoir de cache du tout,
     * il faudra utiliser la directive no-store.
     */
    boolean noCache() default false;

    /**
     * La directive de réponse no-store indique qu'aucun cache (partagé ou privé) ne doit stocker la réponse.
     */
    boolean noStore() default false;

    /**
     * La directive de réponse stale-while-revalidate indique que le cache peut réutiliser
     * une réponse périmée pendant qu'il la revalide dans un cache.
     * <br><br>
     * ex:
     * <code>Cache-Control: max-age=604800, stale-while-revalidate=86400</code>
     * <br><br>
     * Dans l'exemple qui précède, la réponse est fraîche pendant 7 jours (604800s).
     * Après 7 jours, elle devient périmée, mais le cache peut être réutilisé pour les requêtes
     * qui sont faites le jour suivant (86400s), tant que la revalidation de la réponse a lieu en arrière-plan.
     * <br><br>
     * La revalidation rafraîchira le cache à nouveau et la réponse apparaîtra donc comme toujours fraîche
     * aux clients pendant cette période, masquant ainsi la latence induite par une revalidation.
     * <br><br>
     * Si aucune requête n'a lieu pendant cette période intermédiaire,
     * le cache devient périmé et la prochaine requête revalidera le cache normalement.
     */
    long staleWhileRevalidate() default 30;

    /**
     * La directive de réponse private indique que la réponse peut uniquement être enregistrée dans un cache privé
     * (c'est-à-dire le cache local des navigateurs).
     * <br><br>
     * Si private est oubliée pour une réponse avec du contenu personnalisé, cette réponse pourra être enregistrée
     * dans un cache partagé et finir par être réutilisée pour plusieurs personnes,
     * causant ainsi une fuite d'informations personnelles.
     */
    boolean privateCache() default true;

}
