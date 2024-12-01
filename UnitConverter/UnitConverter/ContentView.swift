import SwiftUI

struct ContentView: View {
    @State private var miles: String = ""
    @State private var kilometers: Double = 0.0
    var body: some View {
        ZStack {
            LinearGradient(
                gradient: Gradient(colors: [Color.blue, Color.purple]),
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
            .ignoresSafeArea()

            VStack(spacing: 30) {
                Text("Unit Converter")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .foregroundColor(.white)
                    .padding()

                TextField("Enter miles", text: $miles)
                    .keyboardType(.decimalPad)
                    .padding()
                    .background(Color.white.opacity(0.2))
                    .cornerRadius(10)
                    .foregroundColor(.white)
                    .font(.title2)
                    .padding(.horizontal)

                Button(action: convertToKilometers) {
                    Text("Convert")
                        .fontWeight(.bold)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(LinearGradient(
                            gradient: Gradient(colors: [Color.pink, Color.orange]),
                            startPoint: .leading,
                            endPoint: .trailing))
                        .foregroundColor(.white)
                        .cornerRadius(10)
                        .shadow(radius: 5)
                }
                .padding(.horizontal)

                Text("Kilometers: \(kilometers, specifier: "%.2f")")
                    .font(.title2)
                    .fontWeight(.semibold)
                    .foregroundColor(.white)

                Spacer()
            }
            .padding()
        }
    }

    func convertToKilometers() {
        if let milesValue = Double(miles) {
            kilometers = milesValue * 1.60934
        } else {
            kilometers = 0.0
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
