package dam.pmdm.spyrothedragon.guias;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import dam.pmdm.spyrothedragon.MainActivity;
import dam.pmdm.spyrothedragon.R;

public class GuiaDialogFragment extends DialogFragment {

    // Constantes para las pantallas
    private static final int PANTALLA_INICIAL = 1;
    private static final int PANTALLA_PERSONAJES = 2;
    private static final int PANTALLA_MUNDOS = 3;
    private static final int PANTALLA_COLECCIONABLES = 4;
    private static final int PANTALLA_INFO = 5;
    private static final int PANTALLA_FINAL = 6;

    // Variable para controlar en qué pantalla estamos
    private int pantallaActual = PANTALLA_INICIAL;
    
    // Clave para el argumento de la pantalla inicial
    private static final String ARG_PANTALLA_INICIAL = "pantalla_inicial";

    // Variables para los sonidos
    private MediaPlayer sonidoAvanzar;
    private MediaPlayer sonidoInteraccion;
    private MediaPlayer sonidoFinal;

    // Constructor sin argumentos requerido
    public GuiaDialogFragment() {
    }
    
    /**
     * Crea una nueva instancia de GuiaDialogFragment
     * @param pantallaInicial Pantalla inicial a mostrar
     * @return Una nueva instancia de GuiaDialogFragment
     */
    public static GuiaDialogFragment newInstance(int pantallaInicial) {
        GuiaDialogFragment fragment = new GuiaDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PANTALLA_INICIAL, pantallaInicial);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        
        // Recuperar la pantalla inicial si se ha especificado
        if (getArguments() != null) {
            pantallaActual = getArguments().getInt(ARG_PANTALLA_INICIAL, PANTALLA_INICIAL);
        }
        
        // Inicializar los sonidos
        sonidoAvanzar = MediaPlayer.create(getContext(), R.raw.avanzar);
        sonidoInteraccion = MediaPlayer.create(getContext(), R.raw.interaccion);
        sonidoFinal = MediaPlayer.create(getContext(), R.raw.final_guia);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_guia_dialog, container, false);
        
        // Configurar el diálogo como pantalla completa transparente
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            
            // Evitar que se pueda cerrar tocando fuera o con el botón atrás
            getDialog().setCanceledOnTouchOutside(false);
            getDialog().setOnKeyListener((dialog, keyCode, event) -> 
                keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP);
        }
        
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Cargar el contenido inicial según la pantalla actual
        actualizarContenido();
        
        // Actualizar los indicadores de progreso
        actualizarIndicadoresProgreso();
    }

    private void avanzarPantalla() {
        // Reproducir sonido
        reproducirSonidoAvanzar();
        
        pantallaActual++;
        
        if (pantallaActual <= PANTALLA_FINAL) {
            // Cargar la siguiente pantalla
            actualizarContenido();
        } else {
            // Hemos llegado al final de la guía
            reproducirSonidoFinal();
            finalizarGuia();
        }
    }

    private void finalizarGuia() {
        reproducirSonidoFinal();
        
        // Guardar en SharedPreferences que la guía ha sido completada
        SharedPreferences prefs = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("guiaCompletada", true);
        editor.apply();
        
        // Cerrar el diálogo
        dismiss();
    }

    private void actualizarContenido() {
        // Aquí cambiamos el contenido según la pantalla actual
        View view = getView();
        if (view == null) return;
        
        // Limpiar el contenido anterior
        FrameLayout container = view.findViewById(R.id.contenedorGuia);
        container.removeAllViews();
        
        // Inflar el nuevo contenido según la pantalla actual
        View contenido;
        
        switch (pantallaActual) {
            case PANTALLA_INICIAL:
                contenido = getLayoutInflater().inflate(R.layout.guia_pantalla1, container, false);
                // Configurar botón comenzar con View genérico
                View btnComenzar = contenido.findViewById(R.id.btnComenzar);
                if (btnComenzar != null) {
                    btnComenzar.setOnClickListener(v -> avanzarPantalla());
                }
                break;
            case PANTALLA_PERSONAJES:
                contenido = getLayoutInflater().inflate(R.layout.guia_overlay_personajes, container, false);
                // Configurar botones en esta pantalla
                configurarBotonesNavegacion(contenido);
                break;
            case PANTALLA_MUNDOS:
                contenido = getLayoutInflater().inflate(R.layout.guia_overlay_mundos, container, false);
                // Configurar botones en esta pantalla
                configurarBotonesNavegacion(contenido);
                break;
            case PANTALLA_COLECCIONABLES:
                contenido = getLayoutInflater().inflate(R.layout.guia_overlay_coleccionables, container, false);
                // Configurar botones en esta pantalla
                configurarBotonesNavegacion(contenido);
                break;
            case PANTALLA_INFO:
                contenido = getLayoutInflater().inflate(R.layout.guia_overlay_info, container, false);
                // Configurar botones en esta pantalla
                configurarBotonesNavegacion(contenido);
                break;
            case PANTALLA_FINAL:
                contenido = getLayoutInflater().inflate(R.layout.guia_pantalla6, container, false);
                // Si hay un botón comenzar en la pantalla final, configurarlo
                View btnComenzarFinal = contenido.findViewById(R.id.btnComenzar);
                if (btnComenzarFinal != null) {
                    btnComenzarFinal.setOnClickListener(v -> finalizarGuia());
                }
                break;
            default:
                contenido = new View(requireContext());
                break;
        }
        
        container.addView(contenido);
        
        // Reproducir sonido de interacción cuando se muestra una nueva pantalla (excepto la primera y última)
        if (pantallaActual > PANTALLA_INICIAL && pantallaActual < PANTALLA_FINAL) {
            reproducirSonidoInteraccion();
        }
        
        // Aplicar animaciones si corresponde
        aplicarAnimaciones(contenido);
        
        // Actualizar los indicadores de progreso
        actualizarIndicadoresProgreso();
    }
    
    private void configurarBotonesNavegacion(View contenido) {
        // Buscar los botones Siguiente y Omitir como View genéricos primero
        View btnSiguienteView = contenido.findViewById(R.id.btnSiguiente);
        View btnOmitirView = contenido.findViewById(R.id.btnOmitir);
        //View btnAbrirPantallaView = contenido.findViewById(R.id.btnAbrirPantalla);
        View bocadillo = contenido.findViewById(R.id.bocadillo);
        
        // Configurar el listener para el botón Siguiente
        if (btnSiguienteView != null) {
            btnSiguienteView.setOnClickListener(v -> {
                reproducirSonidoAvanzar();
                avanzarPantalla();
            });
        }
        
        // Configurar el listener para el botón Omitir
        if (btnOmitirView != null) {
            btnOmitirView.setOnClickListener(v -> finalizarGuia());
        }

        // También podemos hacer que el propio bocadillo sea interactivo
        if (bocadillo != null) {
            bocadillo.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // Reducir ligeramente el tamaño al presionar
                            v.setScaleX(0.95f);
                            v.setScaleY(0.95f);
                            return true;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            // Volver al tamaño original al soltar
                            v.setScaleX(1.0f);
                            v.setScaleY(1.0f);
                            return true;
                    }
                    return false;
                }
            });
        }
    }

    private void aplicarAnimaciones(View contenido) {
        switch (pantallaActual) {
            case PANTALLA_INICIAL:
                // Animación de fundido para la pantalla inicial
                Animation fadeIn = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
                contenido.startAnimation(fadeIn);
                break;
            case PANTALLA_PERSONAJES:
            case PANTALLA_MUNDOS:
            case PANTALLA_COLECCIONABLES:
            case PANTALLA_INFO:
                // Animación para el bocadillo
                View bocadillo = contenido.findViewById(R.id.bocadillo);
                if (bocadillo != null) {
                    Animation scaleUp = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
                    bocadillo.startAnimation(scaleUp);
                }
                
                // Animación para la flecha indicadora
                View flecha = contenido.findViewById(R.id.flecha);
                if (flecha != null) {
                    Animation pulseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.pulse_animation);
                    flecha.startAnimation(pulseAnimation);
            
                }
                break;
            case PANTALLA_FINAL:
                // Animación de fundido para la pantalla final
                Animation fadeIn2 = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
                contenido.startAnimation(fadeIn2);
                break;
        }
        // Animar el bocadillo con un efecto de ampliación inicial
    View bocadillo = contenido.findViewById(R.id.bocadillo);
    if (bocadillo != null) {
        // Primero la animación de aparición
        Animation scaleAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.bocadillo_scale);
        bocadillo.startAnimation(scaleAnimation);
        
        // Después de que termine la animación inicial, aplicar el latido continuo
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            
            @Override
            public void onAnimationEnd(Animation animation) {
                Animation latidoAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.bocadillo_latido);
                bocadillo.startAnimation(latidoAnimation);
            }
            
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }
        
        // También podemos animar la flecha indicadora
        View flecha = contenido.findViewById(R.id.flecha);
        if (flecha != null) {
            // Retrasar ligeramente la animación de la flecha para que aparezca después del bocadillo
            flecha.postDelayed(() -> {
                Animation scaleAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.bocadillo_scale);
                flecha.startAnimation(scaleAnimation);
            }, 200);
        }
    }

    private void actualizarIndicadoresProgreso() {
        View view = getView();
        if (view == null) return;
        
        // Actualizar los indicadores según la pantalla actual
        for (int i = 1; i <= 6; i++) {
            int indicadorId = getResources().getIdentifier("indicador" + i, "id", requireContext().getPackageName());
            View indicador = view.findViewById(indicadorId);
            
            if (indicador != null) {
                if (i == pantallaActual) {
                    indicador.setBackgroundResource(R.drawable.indicador_activo);
                } else {
                    indicador.setBackgroundResource(R.drawable.indicador_inactivo);
                }
            }
        }
    }

    // Método para reproducir el sonido de avanzar
    private void reproducirSonidoAvanzar() {
        if (sonidoAvanzar != null) {
            // Reiniciar el sonido si ya está reproduciéndose
            if (sonidoAvanzar.isPlaying()) {
                sonidoAvanzar.seekTo(0);
            } else {
                sonidoAvanzar.start();
            }
        }
    }

    // Método para reproducir el sonido de interacción
    private void reproducirSonidoInteraccion() {
        if (sonidoInteraccion != null) {
            // Reiniciar el sonido si ya está reproduciéndose
            if (sonidoInteraccion.isPlaying()) {
                sonidoInteraccion.seekTo(0);
            } else {
                sonidoInteraccion.start();
            }
        }
    }

    // Método para reproducir el sonido final
    private void reproducirSonidoFinal() {
        if (sonidoFinal != null) {
            // Reiniciar el sonido si ya está reproduciéndose
            if (sonidoFinal.isPlaying()) {
                sonidoFinal.seekTo(0);
            } else {
                sonidoFinal.start();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        
        // Liberar recursos de MediaPlayer
        if (sonidoAvanzar != null) {
            sonidoAvanzar.release();
            sonidoAvanzar = null;
        }
        
        if (sonidoInteraccion != null) {
            sonidoInteraccion.release();
            sonidoInteraccion = null;
        }
        
        if (sonidoFinal != null) {
            sonidoFinal.release();
            sonidoFinal = null;
        }
    }
}
