package com.example.filmoteka;

import android.app.Activity;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

    public class DialogScreen {

        public static final int SWITCH_TO_SERIAS = 1; // Идентификаторы диалоговых окон
        public static final int SWITCH_TO_FILMS = 2;

        public static AlertDialog getDialog(Activity activity) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            int id=0;
            if (activity.getClass().getSimpleName().equalsIgnoreCase("MainActivity")) {
                id = SWITCH_TO_SERIAS;
            }
            else if (activity.getClass().getSimpleName().equalsIgnoreCase("MainActivitySerias")) {
                id = SWITCH_TO_FILMS;
            }
            else {
                Toast.makeText(activity, "Activity name is " + activity.getClass().getSimpleName(), Toast.LENGTH_LONG).show();
            }


            switch(id) {
                case SWITCH_TO_SERIAS: // Диалоговое окно Rate the app
                    builder.setTitle("Выберите коллекцию");
                    builder.setMessage("Сменить коллекцию?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Перейти в СЕРИАЛЫ", new DialogInterface.OnClickListener() { // Переход на оценку приложения
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Переход
                            dialog.dismiss();
                        }
                    });
                    builder.setNeutralButton("Отмена", new DialogInterface.OnClickListener() { // Оценить приложение потом
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); // Отпускает диалоговое окно
                        }
                    });
                    return builder.create();
                case SWITCH_TO_FILMS:
                    builder.setTitle("Выберите коллекцию");
                    builder.setMessage("Сменить коллекцию?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Перейти в ФИЛЬМЫ", new DialogInterface.OnClickListener() { // Переход на оценку приложения
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Переход
                            dialog.dismiss();
                        }
                    });
                    builder.setNeutralButton("Отмена", new DialogInterface.OnClickListener() { // Оценить приложение потом
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); // Отпускает диалоговое окно
                        }
                    });
                    return builder.create();
                default:
                    return null;
            }
        }
    }

