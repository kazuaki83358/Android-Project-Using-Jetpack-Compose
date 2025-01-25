    package com.example.emergencyassistance

    import android.os.Handler
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.*
    import androidx.compose.material3.CircularProgressIndicator
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Brush
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp

    @Composable
    fun SplashScreen(onTimeout: () -> Unit) {
        val customBlue = Color(0xFF00afb9)
        val customCyan = Color(0xFFfdfcdc)

        // Main container with gradient background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.verticalGradient(colors = listOf(customBlue, customCyan))),
            contentAlignment = Alignment.Center
        ) {
            // Column to center the logo and text in the middle of the screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 297.dp), // Add horizontal padding for aesthetics
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo Image
                Image(
                    painter = painterResource(id = R.drawable.logo_s),
                    contentDescription = "Splash Screen Image"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Welcome Text
                Text(
                    text = "Welcome",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                // Spacer to push the loading spinner to the bottom of the screen
                Spacer(modifier = Modifier.weight(1f))

                // Loading Spinner at the bottom
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 32.dp) // Adjust bottom padding for a better layout
                )
            }

            // Simulate a delay for splash screen transition
            LaunchedEffect(Unit) {
                // 2-second delay before navigating
                Handler().postDelayed({
                    onTimeout() // Trigger navigation after the delay
                }, 2000) // 2-second delay
            }
        }
    }
